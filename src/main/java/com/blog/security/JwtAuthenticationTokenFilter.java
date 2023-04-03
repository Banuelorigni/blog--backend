package com.blog.security;

import com.blog.domain.user.UserRepository;
import com.blog.infrastructure.user.entity.UserEntity;
import com.blog.support.constants.SecurityConstants;
import com.blog.support.exceptions.AuthorizationException;
import com.blog.support.exceptions.ErrorCode;
import com.blog.support.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;

    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestUrl = request.getRequestURI();
        for (String pattern : SecurityConfig.IGNORE_URLS) {
            if (antPathMatcher.match(pattern, requestUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        Optional<UserEntity> optionalUser = getEntityOptional(request);
        if (optionalUser.isEmpty()) {
            throw new AuthorizationException(ErrorCode.NOT_EXIST);
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
            throw new AuthorizationException(ErrorCode.ABNORMAL_TOKEN);
        }
    }
}
