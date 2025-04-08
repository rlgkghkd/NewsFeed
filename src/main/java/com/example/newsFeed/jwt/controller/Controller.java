package com.example.newsFeed.jwt.controller;

import com.example.newsFeed.jwt.TokenUtils;
import com.example.newsFeed.jwt.dto.LoginRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;


@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class Controller {

    private final TokenUtils tokenUtils;
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {

        Long id = 1L;

        String accessToken = tokenUtils.createJwt(id);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")
                .maxAge(Duration.ofMinutes(3))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body("로그인 되었습니다.");
    }

    @GetMapping("/open")
    public ResponseEntity<Long> open(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String token = Arrays.stream(cookies)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        Long userIdFromToken = tokenUtils.getUserIdFromToken(token);

        return new ResponseEntity<Long>(userIdFromToken, HttpStatus.OK);
    }
}
