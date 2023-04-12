package com.blog.security;

import com.blog.adapter.user.repository.UserRepository;
import com.blog.infrastructure.user.entity.RoleEntity;
import com.blog.infrastructure.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void should_return_user_details_correctly() {
        String username = "username";
        RoleEntity role = RoleEntity.builder().id(2L).roleName("ADMIN").build();
        UserEntity userEntity = new UserEntity(1L, role, username, "password", username,null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserPrincipalInfo userDetails = (UserPrincipalInfo) userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals(userEntity, userDetails.getUserEntity());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
        assertEquals(userEntity.getUsername(), userDetails.getUsername());
        assertTrue(AuthorityUtils.authorityListToSet(userDetails.getAuthorities()).contains(role.getRoleName()));
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void should_return_user_details_correctly_when_enable_is_false() {
        String username = "username";
        RoleEntity role = RoleEntity.builder().id(2L).roleName("PORTAL_USER").build();
        UserEntity userEntity = new UserEntity(1L, role, username, "password", username,null);
        userEntity.setDeleted(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserPrincipalInfo userDetails = (UserPrincipalInfo) userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals(userEntity, userDetails.getUserEntity());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
        assertEquals(userEntity.getUsername(), userDetails.getUsername());
        assertTrue(AuthorityUtils.authorityListToSet(userDetails.getAuthorities()).contains(role.getRoleName()));
        assertFalse(userDetails.isEnabled());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void should_throw_username_not_found_exception_when_user_details_is_null() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));

        assertEquals("Username: username not found !", exception.getMessage());
    }
}

