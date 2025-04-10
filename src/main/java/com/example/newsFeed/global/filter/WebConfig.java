package com.example.newsFeed.global.filter;

import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenUtils tokenUtils;

    public WebConfig(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Bean
    public FilterRegistrationBean JwtFilter() {

        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(tokenUtils));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}