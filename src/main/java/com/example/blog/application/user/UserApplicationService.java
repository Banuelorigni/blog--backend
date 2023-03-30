package com.example.blog.application.user;

import com.example.blog.application.user.exceptions.UserDuplicateException;
import com.example.blog.domain.user.RoleEnum;
import com.example.blog.domain.user.User;
import com.example.blog.domain.user.UserRepository;
import com.example.blog.infrastructure.user.entity.RoleEntity;
import com.example.blog.security.UserPrincipalInfo;
import com.example.blog.support.exceptions.AuthorizationException;
import com.example.blog.support.exceptions.ErrorCode;
import com.example.blog.support.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        validateForRegister(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPortalRole();

        userRepository.save(user);
    }

    public UserPrincipalInfo generatePrincipal(String username, String password) {
        Authentication authenticate = getAuthentication(username, password);

        return (UserPrincipalInfo) authenticate.getPrincipal();
    }

    public String authenticate(UserPrincipalInfo user, RoleEnum roleEnum) {
        RoleEntity role = user.getUserEntity().getRole();
        if (Objects.isNull(role)) {
            throw new AuthorizationException(ErrorCode.UNKNOWN_ROLE);
        }
        if (!role.getId().equals(roleEnum.getRoleType())) {
            throw new AuthorizationException(ErrorCode.LOGIN_FAILED);
        }
        return jwtUtils.createJwtToken(user.getUserEntity().getId(), role.getRoleName(), user.getUserEntity().getUsername());
    }

    private Authentication getAuthentication(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new AuthorizationException(ErrorCode.LOGIN_FAILED);
        }

        return authenticate;
    }

    private void validateForRegister(String username) {
        if (userRepository.exist(username)) {
            throw new UserDuplicateException(username);
        }
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public String findUserNameByUserId(Long id){
        return userRepository.findById(id).orElseThrow().getUsername();
    }
}
