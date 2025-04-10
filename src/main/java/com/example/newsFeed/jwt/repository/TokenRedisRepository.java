package com.example.newsFeed.jwt.repository;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.entity.TokenRedis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRedisRepository extends JpaRepository<TokenRedis, Long> {
    Optional<TokenRedis> findByUserId(Long userId);

    default TokenRedis findTokenRedisByUserIdOrElseThrow (Long userId) {
        return findByUserId(userId).orElseThrow(() -> new CustomException(Errors.TOKEN_REDIS_NOT_FOUND));
    }

    Optional<TokenRedis> findByRefreshToken(String refreshToken);

    default TokenRedis findTokenRedisByRefreshTokenOrElseThrow(String refreshToken) {
        return findByRefreshToken(refreshToken).orElseThrow(()-> new CustomException(Errors.TOKEN_REDIS_NOT_FOUND));
    }

    void deleteByRefreshToken(String refreshToken);
}
