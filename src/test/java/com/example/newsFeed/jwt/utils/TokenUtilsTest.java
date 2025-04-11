package com.example.newsFeed.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilsTest {

    @DisplayName("accessToken 생성 시 JWT 포맷으로 생성된다")
    @Test
    void createAccessToken_returns_ValidJWTFormat() {
        // given
        Long userId = 1L;
        TokenUtils tokenUtils = new TokenUtils();
        // when
        String token = tokenUtils.createAccessToken(userId);

        // then
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // header.payload.signature
    }

    @DisplayName("accessToken에 userId claim이 포함된다")
    @Test
    void createAccessToken_containsUserIdClaim() {
        // given
        Long userId = 1L;
        TokenUtils tokenUtils = new TokenUtils();

        // when
        String token = tokenUtils.createAccessToken(userId);

        // then
        // 토큰 파싱해서 claim 추출
        Jws<Claims> jwt = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits".getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        Claims claims = jwt.getPayload();

        // userId claim이 존재하고 값이 일치하는지 확인
        assertEquals(userId, claims.get("userId", Integer.class).longValue());
    }

    @DisplayName("accessToken의 만료시간은 정확히 3분 뒤다")
    @Test
    void createAccessToken_hasCorrectExpirationTime() {
// given
        Long userId = 1L;
        TokenUtils tokenUtils = new TokenUtils();
        long expectedMillis = 1000L * 60 * 3; // 3분 = 180,000ms
        long margin = 1000L; // 1초 정도 오차 허용

        // when
        String token = tokenUtils.createAccessToken(userId);

        // then
        Jws<Claims> jwt = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits".getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        Claims claims = jwt.getPayload();
        Date issuedAt = claims.getIssuedAt();      // 발급 시각 (iat)
        Date expiration = claims.getExpiration();  // 만료 시각 (exp)

        long actualMillis = expiration.getTime() - issuedAt.getTime();

        // 발급 시각과 만료 시각의 차이가 정확히 3분(±1초 허용)
        assertTrue(Math.abs(actualMillis - expectedMillis) < margin,
                "만료 시간은 발급 시간 기준 3분이어야 합니다");
    }

}