package com.blog.application.user;

import com.blog.adapter.user.repository.UserRepository;
import com.blog.application.user.exceptions.UserDuplicateException;
import com.blog.domain.user.Role;
import com.blog.domain.user.RoleEnum;
import com.blog.domain.user.User;
import com.blog.infrastructure.user.entity.RoleEntity;
import com.blog.infrastructure.user.entity.UserEntity;
import com.blog.security.UserPrincipalInfo;
import com.blog.support.exceptions.AuthorizationException;
import com.blog.support.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserApplicationServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserApplicationService userApplicationService = new UserApplicationService(userRepository, authenticationManager, jwtUtils, passwordEncoder);

    @Test
    void shouldSaveUserWhenRegisterUserNotExist() {
        String username = "testUsername";
        String password = "P@ssword1";
        String encryptedPassword = "encryptedPassword";
        User user = User.builder().username(username).password(password).build();
        when(passwordEncoder.encode(password)).thenReturn(encryptedPassword);
        when(userRepository.exist(username)).thenReturn(Boolean.FALSE);

        userApplicationService.register(user);

        User savedUser = User.builder().username(username).password(encryptedPassword).role(Role.builder().id(2L).build()).build();
        verify(userRepository, times(1)).save(refEq(savedUser));
    }

    @Test
    void shouldThrowUserDuplicateExceptionWhenUserExist() {
        String username = "testUsername";
        String password = "P@ssword1";
        User user = User.builder().username(username).password(password).build();
        when(userRepository.exist(username)).thenReturn(Boolean.TRUE);

        assertThrows(UserDuplicateException.class, () -> userApplicationService.register(user));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void shouldReturnCorrectToken() {
        UserPrincipalInfo userPrincipalInfo = UserPrincipalInfo.builder()
                .userEntity(new UserEntity(1L, RoleEntity.builder().id(1L).roleName("GENERAL_USER").build(), "correctUsername", "correct_password", "test",null))
                .build();
        when(jwtUtils.createJwtToken(1L, "GENERAL_USER", "correctUsername")).thenReturn("correct_token");

        String token = userApplicationService.authenticate(userPrincipalInfo, RoleEnum.OPERATOR);

        assertEquals("correct_token", token);
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenRoleIsNull() {
        UserPrincipalInfo userPrincipalInfo = UserPrincipalInfo.builder()
                .userEntity(UserEntity.builder().id(1L).username("wrong").password("correct_password").nickname("test").build())
                .build();

        AuthorizationException authorizationException =
                assertThrows(AuthorizationException.class, () -> userApplicationService.authenticate(userPrincipalInfo, RoleEnum.OPERATOR));

        assertEquals("该用户没有权限！", authorizationException.getMessage());
    }

    @Test
    void shouldReturnUserPrincipalInfoCorrectly() {
        String username = "username";
        String password = "password";
        UserPrincipalInfo userPrincipalInfo = UserPrincipalInfo.builder()
                .userEntity(new UserEntity(1L, RoleEntity.builder().id(1L).build(), username, password, username,null))
                .build();
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userPrincipalInfo, password));

        final UserPrincipalInfo result = userApplicationService.generatePrincipal(username, password);

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication value = captor.getValue();
        assertEquals("username", value.getPrincipal().toString());
        assertEquals("password", value.getCredentials().toString());
        assertEquals(result.getUserEntity().getId(), userPrincipalInfo.getUserEntity().getId());
    }

    @Test
    void shouldReturnUserPrincipalInfoCorrectlyOptional() {
        String username = "username";
        String password = "password";
        UserPrincipalInfo userPrincipalInfo = UserPrincipalInfo.builder()
                .userEntity(new UserEntity(1L, RoleEntity.builder().id(2L).build(), username, password, username,null))
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipalInfo);

        UserPrincipalInfo result = userApplicationService.generatePrincipal(username, password);

        assertEquals(result, userPrincipalInfo);
    }

    @Test
    void shouldReturnUserPrincipalInfoWrong() {
        String username = "username";
        String password = "password";
        when(authenticationManager.authenticate(any())).thenReturn(null);

        AuthorizationException authorizationException =
                assertThrows(AuthorizationException.class, () -> userApplicationService.generatePrincipal(username, password));

        assertEquals("用户名或者密码错误！", authorizationException.getMessage());
    }

    @Test
    void shouldThrowBadCredentialExceptionWhenPasswordIsWrong() {
        String username = "username";
        String password = "wrongPassword";
        doThrow(new BadCredentialsException("Bad Credentials")).when(authenticationManager).authenticate(any());

        BadCredentialsException exception =
                assertThrows(BadCredentialsException.class, () -> userApplicationService.generatePrincipal(username, password));

        assertEquals("Bad Credentials", exception.getMessage());
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenRoleNotMatch() {
        UserEntity adminUser = new UserEntity(1L, RoleEntity.builder().id(1L).roleName("GENERAL_USER").build(), "correctUsername", "correct_password", "test",null);
        UserPrincipalInfo adminUserPrincipal = UserPrincipalInfo.builder()
                .userEntity(adminUser)
                .build();

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> userApplicationService.authenticate(adminUserPrincipal, RoleEnum.PORTAL_USER));
        assertEquals(31012, exception.getErrorCode().getCode());
        assertEquals("用户名或者密码错误！", exception.getErrorCode().getMessage());
        verify(jwtUtils, times(0)).createJwtToken(any(), any(), any());
    }

    @Test
    void shouldReturnUserListSuccess() {
        User user1 = User.builder()
                .id(1L)
                .nickname("portal_user1")
                .createdAt(Instant.parse("2020-10-12T13:19:53Z"))
                .build();
        User user2 = User.builder()
                .id(2L)
                .nickname("portal_user2")
                .createdAt(Instant.parse("2021-10-12T13:19:53Z"))
                .build();
        User user3 = User.builder()
                .id(3L)
                .nickname("portal_user3")
                .createdAt(Instant.parse("2022-10-12T13:19:53Z"))
                .build();

        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.DESC, "createdAt");
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user1, user2, user3), pageable, 3));

        Page<User> result = userApplicationService.findAll(pageable);

        assertEquals(15, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());


        List<User> users = result.getContent();
        assertEquals(3, result.getTotalElements());
        assertEquals(3, users.size());

        User user = result.getContent().get(0);
        assertEquals(1L, user.getId());
        assertEquals("portal_user1", user.getNickname());
        assertEquals(Instant.parse("2020-10-12T13:19:53Z"), user.getCreatedAt());
    }
}
