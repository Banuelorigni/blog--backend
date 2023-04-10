package com.blog.adapter.comments.aspect;

import com.blog.support.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class GetUserIdFromCookiesAspect {

    private final JwtUtils jwtUtils;

    @Autowired
    public GetUserIdFromCookiesAspect(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Before("@annotation(getUserIdFromCookies)")
    public void extractUserIdFromCookie(GetUserIdFromCookies getUserIdFromCookies) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        Long userId = Long.valueOf(jwtUtils.getUserIdFromCookies(cookies));
        request.setAttribute("userId", userId);
    }
}

