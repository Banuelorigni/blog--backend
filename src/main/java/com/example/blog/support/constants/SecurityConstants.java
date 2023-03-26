package com.example.blog.support.constants;

public class SecurityConstants {
    public static final String TOKEN_NAME = "asteroid_token";
    public static final String CLAIM_OF_ROLE = "role";
    public static final String CLAIM_OF_USERNAME = "username";
    public static final String COOKIE_NAME = "set-cookie";

    public static String getCookieValue(String jwtToken) {
        return "asteroid_token=" + jwtToken + "; path=/;HttpOnly";
    }
}
