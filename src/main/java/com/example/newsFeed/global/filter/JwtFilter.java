package com.example.newsFeed.global.filter;

import com.example.newsFeed.global.exception.ErrorResponseDto;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.service.UserTokenService;
import com.example.newsFeed.jwt.utils.JwtRequestWrapper;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class JwtFilter implements Filter {

    // 로그인 없이 접근할 수 있는 URL 목록
    private static final String[] WHITE_LIST = {
            "/users",
            "/api/login"
    };

    private final TokenUtils tokenUtils;
    private final UserTokenService userTokenService;

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

        // 화이트리스트가 아닌 경우에만 토큰 검증 로직 실행
        if (!isWhiteList(requestURI)) {

            // accessToken 쿠키에서 추출 (null 가능성 있음)
            String accessToken = tokenUtils.getAccessTokenOrNull(request);

            if (accessToken != null) {
                try {
                    // accessToken 유효성 검증
                    tokenUtils.validateTokenOrThrow(accessToken);

                    // 유효하다면 그대로 다음 필터로 넘김
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;

                } catch (ExpiredJwtException e) {
                    // accessToken 만료된 경우 아래에서 refreshToken 처리
                } catch (Exception e) {
                    // accessToken 유효하지 않은 경우 바로 응답
                    handleTokenException(response, e, "AccessToken");
                    return;
                }
            }

            // accessToken이 없거나 만료된 경우 → refreshToken 처리
            try {
                // refreshToken이 없거나 쿠키가 없으면 여기서 예외 발생
                String refreshToken = tokenUtils.getRefreshToken(request);

                try {
                    // refreshToken 유효성 검증
                    tokenUtils.validateTokenOrThrow(refreshToken);

                    // DB에서 userId 조회 후 accessToken 재발급
                    Long userId = userTokenService.findUserIdByRefreshToken(refreshToken);
                    String newAccessToken = tokenUtils.createAccessToken(userId);

                    // 새 accessToken을 쿠키에 담아 응답
                    tokenUtils.refreshAccessTokenCookie(response, newAccessToken);

                    // wrappedRequest에 새로운 accessToken을 반영하여 다음 필터로 넘김
                    HttpServletRequest wrappedRequest = new JwtRequestWrapper(request, newAccessToken);
                    filterChain.doFilter(wrappedRequest, servletResponse);
                    return;

                } catch (ExpiredJwtException ex) {
                    // refreshToken도 만료되었으면 DB에서 삭제 + 재로그인 요청
                    userTokenService.deleteUserTokenByRefreshToken(refreshToken);
                    handleTokenException(response, ex, "RefreshToken");
                    return;

                } catch (Exception ex) {
                    // 기타 refreshToken 예외
                    handleTokenException(response, ex, "RefreshToken");
                    return;
                }

            } catch (Exception e) {
                // refreshToken 쿠키 없음 등
                handleTokenException(response, e, "RefreshToken");
                return;
            }
        }

        // 화이트리스트인 경우엔 바로 다음 필터로 넘김
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // JWT 관련 예외를 공통 포맷으로 응답
    private void handleTokenException(HttpServletResponse response, Exception ex, String tokenType) throws IOException {
        Errors error = Errors.fromException(ex);
        String message = tokenType + ": " + error.getMessage();
        makeErrorResponseToJson(response, error, message);
    }

    private void makeErrorResponseToJson(HttpServletResponse response, Errors error, String errorMessage) throws IOException {
        ErrorResponseDto errorResponse = ErrorResponseDto.of(error, errorMessage);

        response.setStatus(error.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }

    // URI가 화이트리스트에 포함되는지 검사
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
