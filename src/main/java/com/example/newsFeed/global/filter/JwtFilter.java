package com.example.newsFeed.global.filter;

import com.example.newsFeed.global.exception.ErrorResponseDto;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.service.TokenRedisService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

import static com.example.newsFeed.jwt.utils.TokenUtils.getAccessToken;
import static com.example.newsFeed.jwt.utils.TokenUtils.getRefreshToken;

public class JwtFilter implements Filter {

    // 로그인 없이 접근할 수 있는 URL 목록
    private static final String[] WHITE_LIST = {
            "/users",
            "/api/login"
    };

    // TokenUtils 클래스 의존성 주입
    private final TokenUtils tokenUtils;

    private final TokenRedisService tokenRedisService;

    // TokenUtils 클래스 의존성 주입을 위한 생성자 autoWired X
    public JwtFilter(TokenUtils tokenUtils, TokenRedisService tokenRedisService) {
        this.tokenUtils = tokenUtils;
        this.tokenRedisService = tokenRedisService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        // 화이트리스트는 인증 없이 통과
        if (isWhiteList(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String accessToken = getAccessToken(request);

        // accessToken null 검증
        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "AccessToken이 존재하지 않습니다.");
            return;
        }
        try {
            // accessToken 유효성 검증
            tokenUtils.validateTokenOrThrow(accessToken);

            // accessToken 만료 → refreshToken으로 재발급 시도
        } catch (ExpiredJwtException e) {
            String refreshToken = getRefreshToken(request);

            // refreshToken null 검증
            if (refreshToken == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "RefreshToken이 존재하지 않습니다.");
                return;
            }
            try {
                //refreshToken 유효성 검증
                tokenUtils.validateTokenOrThrow(refreshToken);

                //refreshToken으로 Db에서 TokenRedis를 찾은 후 userId를 추출
                Long userId = tokenRedisService.findUserIdByRefreshToken(refreshToken);

                // 추출된 userId로 재발급용 accessToken을 생성
                String newAccessToken = tokenUtils.createAccessToken(userId);

                // access토큰을 쿠키에 담아 HttpServletResponse Header에 재발급
                tokenUtils.refreshAccessTokenCookie(response, newAccessToken);

                // refreshToken이 만료가 된 경우 Db의 TokenRedis를 삭제하고 재로그인 요구 응답
            } catch (ExpiredJwtException ex) {
                tokenRedisService.deleteTokenRedisByFresh(refreshToken);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "RefreshToken이 만료되었습니다. 다시 로그인 해주세요.");
                return;

                // 만료 이외의 refreshToken 관련 예외들은 각 예외에 따른 메세지를 응답(HttpStatus.403)
            } catch (Exception ex) {
                handleTokenException(response, ex, "RefreshToken");
                return;
            }

            // 만료 이외의 accessToken 관련 예외들은 각 예외에 따른 메세지를 응답(HttpStatus.403)
        } catch (Exception ex) {
            handleTokenException(response, ex, "AccessToken");
            return;
        }

        // 다음 Filter가 없으므로 dispatcher Servlet을 호출
        filterChain.doFilter(servletRequest, servletResponse);
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