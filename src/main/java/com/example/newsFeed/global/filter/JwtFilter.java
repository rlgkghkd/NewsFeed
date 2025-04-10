package com.example.newsFeed.global.filter;

import com.example.newsFeed.jwt.utils.TokenUtils;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseCookie;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class JwtFilter implements Filter {

    private static final String[] WHITE_LIST = {
            "/users",
            "/login"
    };

    private final TokenUtils tokenUtils;

    public JwtFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        if (!isWhiteList(requestURI)) {
            Cookie[] cookies = request.getCookies();
            String accessToken = extractTokenFromCookies(cookies, "accessToken");

            if (accessToken == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessToken이 존재하지 않습니다.");
                return;
            }

            try {
                // 액세스 토큰 검증
                tokenUtils.validateTokenOrThrow(accessToken);

            } catch (ExpiredJwtException e) {
                // 액세스 토큰이 만료된 경우, 리프레시 토큰을 이용해 재발급 시도
                String refreshToken = extractTokenFromCookies(cookies, "refreshToken");

                if (refreshToken == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "RefreshToken이 존재하지 않습니다.");
                    return;
                }

                try {
                    tokenUtils.validateTokenOrThrow(refreshToken);
                    // Db에 있는 TokenRedis를 찾은 후 accessToken 재발급
                    Long userId = tokenUtils.findUserIdByRefreshToken(refreshToken);
                    String newAccessToken = tokenUtils.createAccessToken(userId);

                        ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofMinutes(3))
                                .sameSite("Strict")
                                .build();


                } catch (ExpiredJwtException ex) {
                    tokenUtils.deleteTokenRedis(refreshToken);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "RefreshToken이 만료되었습니다.");
                    return;
                } catch (JwtException | IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 RefreshToken입니다.");
                    return;
                } catch (DataAccessException ex) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Redis 데이터 접근 실패.");
                    return;
                } catch (NoSuchElementException ex) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "해당 RefreshToken 값이 Redis에 없습니다.");
                    return;
                }

            } catch (SecurityException | MalformedJwtException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "잘못된 JWT 서명입니다.");
                return;
            } catch (UnsupportedJwtException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "지원하지 않는 JWT입니다.");
                return;
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "토큰 형식이 올바르지 않습니다.");
                return;
            }
        }

        // 화이트리스트거나 인증 절차 통과 시 다음 필터로
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    private String extractTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
