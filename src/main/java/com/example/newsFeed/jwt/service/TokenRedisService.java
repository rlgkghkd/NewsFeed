package com.example.newsFeed.jwt.service;

import com.example.newsFeed.jwt.entity.TokenRedis;
import com.example.newsFeed.jwt.repository.TokenRedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import static com.example.newsFeed.jwt.utils.TokenUtils.getAccessToken;
import static com.example.newsFeed.jwt.utils.TokenUtils.getUserIdFromToken;

@RequiredArgsConstructor
@Service
public class TokenRedisService {

    private final TokenRedisRepository tokenRedisRepository;

    public void saveRefreshInDb(Long userId, String refreshToken) {
        TokenRedis tokenRedis = TokenRedis.toEntity(userId, refreshToken);
        tokenRedisRepository.save(tokenRedis);
    }

    public void deleteTokenById(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        Long id = getUserIdFromToken(accessToken);
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
