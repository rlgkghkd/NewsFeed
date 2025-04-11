package com.example.newsFeed.global.filter;

import com.example.newsFeed.jwt.service.TokenRedisService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenUtils tokenUtils;
    private final TokenRedisService tokenRedisService;


    public WebConfig(TokenUtils tokenUtils, TokenRedisService tokenRedisService) {
        this.tokenUtils = tokenUtils;
        this.tokenRedisService = tokenRedisService;
    }

    @Bean
    public FilterRegistrationBean JwtFilter() {

        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(tokenUtils, tokenRedisService));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}