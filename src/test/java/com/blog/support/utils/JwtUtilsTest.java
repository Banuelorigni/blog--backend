package com.blog.support.utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static com.blog.support.constants.SecurityConstants.CLAIM_OF_ROLE;
import static com.blog.support.constants.SecurityConstants.CLAIM_OF_USERNAME;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secret", "test_secret");
        ReflectionTestUtils.setField(jwtUtils, "expirationTime", 1800000);
    }

    @Test
    void shouldCreateJwtTokenAndParseJwtToken() {
        String jwtToken = jwtUtils.createJwtToken(1L, "test_role", "test_username");
        Claims claims = jwtUtils.parseJwtToken(jwtToken);

        assertEquals(1L, Long.valueOf(claims.getId()));
        assertEquals("test_role", claims.get(CLAIM_OF_ROLE));
        assertEquals("test_username", claims.get(CLAIM_OF_USERNAME));
    }

    @Test
    void shouldGetUserIdFromCookiesCorrectly() {

        String jwtToken = jwtUtils.createJwtToken(1L, "test_role", "test_username");

        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("blog_token", jwtToken);

        Long userId = Long.parseLong(jwtUtils.getUserIdFromCookies(cookies));
        assertEquals(1L, userId);
    }

}
