package com.example.newsFeed.jwt.service;

import com.example.newsFeed.jwt.entity.TokenRedis;
import com.example.newsFeed.jwt.repository.TokenRedisRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenRedisService {

    private final TokenRedisRepository tokenRedisRepository;
    private final TokenUtils tokenUtils;

    public void saveRefreshInDb(Long userId, String refreshToken) {
        TokenRedis tokenRedis = TokenRedis.toEntity(userId, refreshToken);
        tokenRedisRepository.save(tokenRedis);
    }

    public void deleteTokenById(HttpServletRequest request) {
        String accessToken = tokenUtils.getAccessToken(request);
        Long id = tokenUtils.getUserIdFromToken(accessToken);
        tokenRedisRepository.deleteById(id);
    }

    public Long findUserIdByRefreshToken(String refreshToken)
            throws DataAccessException {
        return tokenRedisRepository.findTokenRedisByRefreshTokenOrElseThrow(refreshToken).getUserId();
    }

    // Db에 저장된 Refresh Token 삭제
    public void deleteTokenRedisByFresh(String refreshToken) {
        tokenRedisRepository.deleteByRefreshToken(refreshToken);
    }
}
