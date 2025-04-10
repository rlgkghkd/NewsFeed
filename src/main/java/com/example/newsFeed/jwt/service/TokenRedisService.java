package com.example.newsFeed.jwt.service;

import com.example.newsFeed.jwt.entity.TokenRedis;
import com.example.newsFeed.jwt.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenRedisService {

    private final TokenRedisRepository tokenRedisRepository;

    public void saveRefreshInDb(Long userId, String refreshToken) {
        TokenRedis tokenRedis = TokenRedis.toEntity(userId, refreshToken);
        tokenRedisRepository.save(tokenRedis);
    }
}
