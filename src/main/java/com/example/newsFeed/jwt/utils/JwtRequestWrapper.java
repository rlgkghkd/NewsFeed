package com.example.newsFeed.jwt.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtRequestWrapper extends HttpServletRequestWrapper {


    private final Cookie[] cookies;

    public JwtRequestWrapper(HttpServletRequest request, String newAccessToken) {
        super(request);

        // 기존 쿠키 배열을 복사하고, accessToken만 새로 덮어씀
        List<Cookie> cookieList = new ArrayList<>();
        if (request.getCookies() != null) {
            cookieList.addAll(Arrays.asList(request.getCookies()));
        }

        // 기존 accessToken 쿠키는 제거
        cookieList.removeIf(cookie -> "accessToken".equals(cookie.getName()));

        // 새 accessToken 쿠키 생성 (서버 측 쿠키)
        Cookie newTokenCookie = new Cookie("accessToken", newAccessToken);
        newTokenCookie.setPath("/");
        newTokenCookie.setHttpOnly(true);
        newTokenCookie.setSecure(true); // 실제 배포 시 true
        newTokenCookie.setMaxAge(3 * 60); // 3분
        cookieList.add(newTokenCookie);
        this.cookies = cookieList.toArray(new Cookie[0]);
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }
}