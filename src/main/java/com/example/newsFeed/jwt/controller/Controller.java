package com.example.newsFeed.jwt.controller;

import com.example.newsFeed.jwt.service.TokenRedisService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.jwt.dto.LoginRequestDto;
import com.example.newsFeed.jwt.dto.LoginResponseDto;
import com.example.newsFeed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class Controller {

    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final TokenRedisService tokenRedisService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        Long id = userService.login(requestDto);

        String accessToken = tokenUtils.createAccessToken(id);
        String refreshToken = tokenUtils.createRefreshToken();

        tokenRedisService.saveRefreshInDb(id, refreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofMinutes(3))
                .sameSite("Strict")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new LoginResponseDto("로그인 되었습니다."));
    }
}
