package com.example.newsFeed.jwt.utils;

import com.example.newsFeed.global.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.lettuce.core.RedisCommandTimeoutException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.Token;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class TokenUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenUtils(RedisTemplate<String, String> redisTemplete) {
        this.redisTemplate = redisTemplete;
    }

    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    private static final SecretKey key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

    // 액세스 토큰 생성
    public String createAccessToken(Long userId) {
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
                .expiration(new Date(now.getTime() + 1000L * 60 * 60)) // 1시간
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

    public String getAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        return Arrays.stream(cookies)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    // 만료일이 현재 날짜 이전인 경우 Jwt예외를 throws 한다.
    public void validateTokenOrThrow(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

        if (claims.getPayload().getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, null, "토큰이 만료되었습니다.");
        }
    }

    // 예외들 enum 추가 필요
    public boolean isRefreshTokenInRedis(String refreshToken)
            throws DataAccessException, RedisConnectionFailureException, RedisCommandTimeoutException {

        return redisTemplate.opsForValue().get(refreshToken) != null;
    }

    // Redis에 저장된 Refresh Token 삭제
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    // Exception Customizing 필요
    public Long extracatRedisValue(String refreshToken) throws NoSuchElementException {
        String id = redisTemplate.opsForValue().get(refreshToken);
        if (id != null) {
            return Long.parseLong(id);
        } else {
            throw new NoSuchElementException("해당 refreshToken에 해당하는 값이 Redis에 존재하지 않습니다.");
        }
    }
}