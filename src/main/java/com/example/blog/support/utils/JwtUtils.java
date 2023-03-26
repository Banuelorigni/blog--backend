package com.example.blog.support.utils;

import com.example.blog.support.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static com.example.blog.support.constants.SecurityConstants.CLAIM_OF_ROLE;
import static com.example.blog.support.constants.SecurityConstants.CLAIM_OF_USERNAME;

@Getter
@Setter
@Component
public class JwtUtils {

    @Value("${authorization.jwt.secret}")
    private String secret;
    @Value("${authorization.jwt.expires}")
    private long expirationTime;

    public String createJwtToken(Long id, String role, String username) {
        String encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.builder()
                .setId(String.valueOf(id))
                .claim(CLAIM_OF_ROLE, role)
                .claim(CLAIM_OF_USERNAME, username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, encodedSecret)
                .compact();

    }

    public Claims parseJwtToken(String jwt) {
        return Jwts
                .parser()
                .setSigningKey(secret.getBytes(Charset.defaultCharset()))
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String getUserIdFromCookies(Cookie[] cookies) {
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(SecurityConstants.TOKEN_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        Claims claims = parseJwtToken(token);
        return claims.getId();
    }
}
