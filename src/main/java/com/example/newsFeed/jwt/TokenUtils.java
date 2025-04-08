package com.example.newsFeed.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class TokenUtils {

    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    private static final SecretKey key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

    // 액세스 토큰 생성
    public String createJwt(int userId) {
        Date now = new Date();
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 1000L * 60 * 3)) // 3분
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 1000L * 60 * 60 * 24)) // 1일
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // JWT에서 userId 추출
    public Long getUserIdFromToken(String token) {
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            Claims claims = (Claims) jwt.getPayload();
            return claims.get("userId", Integer.class).longValue();
        } catch (JwtException e) {
            // 만료, 서명 오류 등
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
