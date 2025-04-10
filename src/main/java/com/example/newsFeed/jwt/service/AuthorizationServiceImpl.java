package com.example.newsFeed.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshInRedis(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(refreshToken, String.valueOf(userId), 1, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("RT:" + userId, refreshToken, 1, TimeUnit.HOURS);
    }
}
