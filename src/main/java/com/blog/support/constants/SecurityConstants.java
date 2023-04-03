package com.blog.support.constants;

public class SecurityConstants {
    public static final String TOKEN_NAME = "blog_token";
    public static final String CLAIM_OF_ROLE = "role";
    public static final String CLAIM_OF_USERNAME = "username";
    public static final String COOKIE_NAME = "set-cookie";

    public static String getCookieValue(String jwtToken) {
        return "blog_token=" + jwtToken + "; path=/;HttpOnly; SameSite=None; Secure";
    }
}
