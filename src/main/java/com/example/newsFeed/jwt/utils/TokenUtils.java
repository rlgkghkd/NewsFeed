package com.example.newsFeed.jwt.utils;

import com.example.newsFeed.jwt.repository.TokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    TokenRedisRepository tokenRedisRepository;

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
    public static Long getUserIdFromToken(String token) {
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

    public static String getAccessToken(HttpServletRequest request) {

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
    public Long findUserIdByRefreshToken(String refreshToken)
            throws DataAccessException {
        return tokenRedisRepository.findTokenRedisByRefreshTokenOrElseThrow(refreshToken).getUserId();
    }

    // Redis에 저장된 Refresh Token 삭제
    public void deleteTokenRedis(String refreshToken) {
        tokenRedisRepository.deleteByRefreshToken(refreshToken);
    }

}