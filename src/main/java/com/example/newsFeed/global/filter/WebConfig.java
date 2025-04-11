package com.example.newsFeed.global.filter;

import com.example.newsFeed.jwt.service.TokenRedisService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // JwtFilter에서 사용할 TokenUtils, TokenRedisService를 주입
    private final TokenUtils tokenUtils;
    private final TokenRedisService tokenRedisService;

    // JwtFilter를 Spring 컨테이너에 등록하기 위한 Bean 정의
    @Bean
    public FilterRegistrationBean JwtFilter() {

        // 필터 등록을 위한 FilterRegistrationBean 생성
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // JwtFilter 인스턴스를 생성하여 등록 (의존성 주입 포함)
        filterRegistrationBean.setFilter(new JwtFilter(tokenUtils, tokenRedisService));

        // 필터의 실행 순서 설정 (숫자가 낮을수록 먼저 실행됨)
        filterRegistrationBean.setOrder(1);

        // 이 필터가 적용될 URL 패턴 설정 - 모든 요청에 적용
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}