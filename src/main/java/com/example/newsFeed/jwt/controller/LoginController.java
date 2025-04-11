package com.example.newsFeed.jwt.controller;

import com.example.newsFeed.jwt.dto.LogoutResponseDto;
import com.example.newsFeed.jwt.service.TokenRedisService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.jwt.dto.LoginRequestDto;
import com.example.newsFeed.jwt.dto.LoginResponseDto;
import com.example.newsFeed.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RequiredArgsConstructor
@RestController
public class LoginController {

    // JWT 생성 및 쿠키 처리 유틸
    private final TokenUtils tokenUtils;
    // 사용자 관련 서비스
    private final UserService userService;
    // RefreshToken 저장 및 삭제 처리 서비스
    private final TokenRedisService tokenRedisService;

    /**
     * 로그인 요청 처리
     * 사용자 인증 후 AccessToken, RefreshToken을 발급하고
     * 이를 쿠키로 클라이언트에 전달
     */
    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        // 사용자 인증 후 userId 반환
        Long id = userService.login(requestDto);

        // AccessToken 및 RefreshToken 생성
        String accessToken = tokenUtils.createAccessToken(id);
        String refreshToken = tokenUtils.createRefreshToken();

        // RefreshToken을 DB에 저장 (userId와 함께)
        tokenRedisService.saveRefreshInDb(id, refreshToken);

        // AccessToken 쿠키 생성 (HttpOnly, Secure, SameSite 설정 포함)
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofMinutes(3))
                .sameSite("Strict")
                .build();

        // RefreshToken 쿠키 생성 (HttpOnly, Secure, SameSite 설정 포함)
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        // 응답 헤더에 쿠키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // 로그인 성공 응답
        return ResponseEntity.ok()
                .headers(headers)
                .body(new LoginResponseDto("로그인 되었습니다."));
    }

    /**
     * 로그아웃 요청 처리
     * RefreshToken DB에서 삭제하고, 클라이언트의 쿠키도 제거
     */
    @DeleteMapping("/api/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) {

        // RefreshToken DB에서 삭제
        tokenRedisService.deleteTokenById(request);

        // AccessToken 쿠키 삭제 (maxAge 0으로 설정)
        ResponseCookie accessTokenCookie = tokenUtils.deleteAccessCookie();

        // RefreshToken 쿠키 삭제 (maxAge 0으로 설정)
        ResponseCookie refreshTokenCookie = tokenUtils.deleteRefreshCookie();

        //헤더에 기간이 0인 쿠키들 등록
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok().headers(headers).body(new LogoutResponseDto("정상적으로 로그아웃 되셨습니다."));
    }
}
