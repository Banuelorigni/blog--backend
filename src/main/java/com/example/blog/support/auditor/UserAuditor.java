package com.example.blog.support.auditor;

import com.example.blog.infrastructure.user.entity.UserEntity;
import com.example.blog.security.UserPrincipalInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditor implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
            UserEntity userPrincipal = (UserEntity) authentication.getPrincipal();
            return Optional.of(userPrincipal.getUsername());
        } else {
            return Optional.empty();
        }
    }

}
