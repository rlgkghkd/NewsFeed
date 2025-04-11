package com.example.newsFeed.jwt.utils;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenUtils {

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

    //HttpServletRequest에서 accessToken을 가져오는 메서드
    public String getAccessToken(HttpServletRequest request) {
        return  getTokenWithVerification(request, "accessToken");
    }
    //HttpServletRequest에서 refreshToken을 가져오는 메서드
    public String getRefreshToken(HttpServletRequest request) {
        return getTokenWithVerification(request, "refreshToken");
    }
    //HttpServletRequest에서 매개 타입별 Token을 가져오는 메서드
public String getTokenWithVerification(HttpServletRequest request, String tokenType) {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
        throw new CustomException(Errors.UNAUTHORIZED_ACCESS, "다시 로그인해주세요");
    }

    return Arrays.stream(cookies)
            .filter(cookie -> tokenType.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(() -> new CustomException(Errors.UNAUTHORIZED_ACCESS, tokenType + " 쿠키가 존재하지 않습니다. 재요청해주세요!"));
}

    // token을 parsing하면서 그 외 Jwt예외들 throw 한다.
    public void validateTokenOrThrow(String token) throws JwtException {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public void refreshAccessTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(3))
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}