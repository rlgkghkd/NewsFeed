package com.example.newsFeed.jwt.service;

import com.example.newsFeed.jwt.entity.UserToken;
import com.example.newsFeed.jwt.repository.UserTokenRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final TokenUtils tokenUtils;

    public void saveRefreshInDb(Long userId, String refreshToken) {
        UserToken userToken = UserToken.toEntity(userId, refreshToken);
        userTokenRepository.save(userToken);
    }

    public void deleteTokenById(HttpServletRequest request) {
        String accessToken = tokenUtils.getAccessToken(request);
        Long id = tokenUtils.getUserIdFromToken(accessToken);
        userTokenRepository.deleteById(id);
    }

    public Long findUserIdByRefreshToken(String refreshToken)
            throws DataAccessException {
        return userTokenRepository.findUserTokenByRefreshTokenOrElseThrow(refreshToken).getUserId();
    }

    // Db에 저장된 Refresh Token 삭제
    public void deleteUserTokenByRefreshToken(String refreshToken) {
        userTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
