package com.example.newsFeed.jwt.controller;

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


    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        Long id = userService.login(requestDto);

        String accessToken = tokenUtils.createAccessToken(id);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofMinutes(3))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(new LoginResponseDto("로그인 되었습니다."));
    }
}
