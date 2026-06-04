package com.malaika.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey12";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Long userId) {

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSignKey())
                .compact();
    }
    public Long extractUserId(String token) {
        return Long.valueOf(
                extractAllClaims(token).getSubject()
        );
    }

    public boolean isTokenValid(String token, Long userId) {

        Long extractedUserId = extractUserId(token);

        return extractedUserId.equals(userId)
                && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}