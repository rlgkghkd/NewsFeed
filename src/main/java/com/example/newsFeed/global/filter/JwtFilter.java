package com.example.newsFeed.global.filter;

import com.example.newsFeed.global.exception.ErrorResponseDto;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.service.UserTokenService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.PatternMatchUtils;
import java.io.IOException;
import java.util.Optional;

public class JwtFilter implements Filter {

    // 로그인 없이 접근할 수 있는 URL 목록
    private static final String[] WHITE_LIST = {
            "/users",
            "/api/login"
    };

    // TokenUtils 클래스 의존성 주입
    private final TokenUtils tokenUtils;

    private final UserTokenService userTokenService;

    // TokenUtils 클래스 의존성 주입을 위한 생성자 autoWired X
    public JwtFilter(TokenUtils tokenUtils, UserTokenService userTokenService) {
        this.tokenUtils = tokenUtils;
        this.userTokenService = userTokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        // 화이트리스트 경로는 토큰 검증 없이 바로 통과
        if (isWhiteList(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // accessToken 쿠키에서 추출 (null일 수 있음)
        String accessToken = tokenUtils.getAccessTokenOrNull(request);

        // accessToken이 존재하고 유효한 경우 → 그대로 다음 필터 진행
        if (accessToken != null) {
            try {
                tokenUtils.validateTokenOrThrow(accessToken);
                filterChain.doFilter(servletRequest, servletResponse);
                return;

            } catch (ExpiredJwtException e) {
                // accessToken이 만료되었을 경우 → refreshToken으로 재발급 시도 (아래에서 처리)
            } catch (Exception e) {
                // accessToken이 만료 외의 문제로 유효하지 않으면 → 바로 에러 응답
                handleTokenException(response, e, "AccessToken");
                return;
            }
        }

        // accessToken이 없거나 만료되었을 경우 → refreshToken 처리
        // 책임을 분리하기 위해서 refreshToken 유효성 검증 부분과 try-catch를 나누었다.
        // 분기별로 다른 로직을 확장할 여지 또한 남기기 위함도 있다.
        try {
            // refreshToken이 존재하지 않거나 쿠키가 없을 경우 여기서 예외 발생 → 재로그인 요구
            String refreshToken = tokenUtils.getRefreshToken(request);

            try {
                // refreshToken 유효성 검증
                tokenUtils.validateTokenOrThrow(refreshToken);

                // DB에서 userId 조회
                Long userId = userTokenService.findUserIdByRefreshToken(refreshToken);

                // accessToken 재발급
                String newAccessToken = tokenUtils.createAccessToken(userId);

                // 재발급된 accessToken을 쿠키에 담아 응답
                tokenUtils.refreshAccessTokenCookie(response, newAccessToken);

                // 재발급 후 다음 필터로 넘김
                filterChain.doFilter(servletRequest, servletResponse);
                return;

            } catch (ExpiredJwtException ex) {
                // refreshToken이 만료되었을 경우 → DB에서 토큰 삭제 + 재로그인 요구
                userTokenService.deleteUserTokenByRefreshToken(refreshToken);
                handleTokenException(response, ex, "RefreshToken");
                return;

            } catch (Exception ex) {
                // refreshToken에 대해 다른 문제가 발생한 경우 → 예외 응답
                handleTokenException(response, ex, "RefreshToken");
                return;
            }

        } catch (Exception e) {
            // refreshToken 자체가 없거나 쿠키가 없어서 예외가 발생한 경우
            handleTokenException(response, e, "RefreshToken");
        }
    }

    // Jwts예외를 공통적으로 처리하여 메시지 전송
    private void handleTokenException(HttpServletResponse response, Exception ex, String tokenType) throws IOException {
        Errors error = Errors.fromException(ex);

        // 커스터마이즈된 메시지 (선택)
        String message = tokenType + ": " + error.getMessage();

        makeErrorResponseToJason(response, error, message);
    }

    private void makeErrorResponseToJason(HttpServletResponse response, Errors error, String errorMessage) throws IOException {

        // ErrorResponseDto 생성
        ErrorResponseDto errorResponse = ErrorResponseDto.of(error, errorMessage);

        // JSON 응답 세팅
        response.setStatus(error.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Jackson ObjectMapper를 이용해 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }

    // 요청 URI가 화이트리스트에 포함되어 있는지 확인
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}