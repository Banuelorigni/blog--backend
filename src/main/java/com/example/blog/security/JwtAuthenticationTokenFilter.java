package com.example.blog.security;

import com.example.blog.domain.user.UserRepository;
import com.example.blog.infrastructure.user.entity.UserEntity;
import com.example.blog.support.constants.SecurityConstants;
import com.example.blog.support.exceptions.AuthorizationException;
import com.example.blog.support.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.blog.security.SecurityConfig.IGNORE_URLS;
import static com.example.blog.support.exceptions.ErrorCode.ABNORMAL_TOKEN;
import static com.example.blog.support.exceptions.ErrorCode.NOT_EXIST;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestUrl = request.getRequestURI();
        for (String pattern : IGNORE_URLS) {
            if (antPathMatcher.match(pattern, requestUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        Optional<UserEntity> optionalUser = getEntityOptional(request);
        if (optionalUser.isEmpty()) {
            throw new AuthorizationException(NOT_EXIST);
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(optionalUser.get().getRole().getRoleName()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(optionalUser.get(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private Optional<UserEntity> getEntityOptional(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        try {
            String token = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(SecurityConstants.TOKEN_NAME))
                    .findFirst().get().getValue();

            Claims claims = jwtUtils.parseJwtToken(token);
            String id = claims.getId();
            return userRepository.findById(Long.valueOf(id));
        } catch (Exception e) {
            throw new AuthorizationException(ABNORMAL_TOKEN);
        }
    }
}
