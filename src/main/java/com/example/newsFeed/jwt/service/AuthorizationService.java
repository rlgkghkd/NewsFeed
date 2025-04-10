package com.example.newsFeed.jwt.service;

public interface AuthorizationService {

    public void saveRefreshInRedis(Long userId, String refreshToken);
}
