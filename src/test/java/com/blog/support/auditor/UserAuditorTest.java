package com.blog.support.auditor;

import com.blog.infrastructure.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuditorTest {

    private UserAuditor userAuditor;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userAuditor = new UserAuditor();
    }

    @Test
    public void get_current_auditor() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Optional<String> currentAuditor = userAuditor.getCurrentAuditor();
        assertTrue(currentAuditor.isPresent());
        assertEquals(currentAuditor.get(), username);
    }

    @Test
    public void get_current_auditor_with_null_authentication() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        Optional<String> currentAuditor = userAuditor.getCurrentAuditor();
        assertFalse(currentAuditor.isPresent());
    }

    @Test
    public void get_current_auditor_with_nonUser_principal() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new Object());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Optional<String> currentAuditor = userAuditor.getCurrentAuditor();
        assertFalse(currentAuditor.isPresent());
    }
}
