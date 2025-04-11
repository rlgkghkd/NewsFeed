package com.example.newsFeed.jwt.service;

import com.example.newsFeed.jwt.entity.UserToken;
import com.example.newsFeed.jwt.repository.UserTokenRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final TokenUtils tokenUtils;

    //주어진 userId와 refreshToken을 기반으로 UserToken 엔티티를 생성하고 DB에 저장
    @Transactional
    public void saveRefreshInDb(Long userId, String refreshToken) {
        UserToken userToken = UserToken.toEntity(userId, refreshToken);
        userTokenRepository.save(userToken);
    }

    //요청에서 accessToken을 꺼내고, 토큰에서 추출한 userId를 기반으로 DB에서 해당 유저의 토큰을 삭제
    @Transactional
    public void deleteTokenById(HttpServletRequest request) {
        String accessToken = tokenUtils.getAccessToken(request);
        Long id = tokenUtils.getUserIdFromToken(accessToken);
        userTokenRepository.deleteById(id);
    }

    //주어진 refreshToken으로 DB에서 UserToken을 찾아 해당 userId 반환
    //refreshToken을 통해 재발급 시 유저 식별을 위해 사용
    //throws DataAccessException DB 조회 중 오류가 발생할 경우 예외 발생
    public Long findUserIdByRefreshToken(String refreshToken)
            throws DataAccessException {
        return userTokenRepository.findUserTokenByRefreshTokenOrElseThrow(refreshToken).getUserId();
    }

     //주어진 refreshToken으로 UserToken을 조회한 후, 해당 토큰 정보를 DB에서 삭제
     // refreshToken 만료 또는 재로그인 유도 시 사용
     // throws DataAccessException DB 조회 또는 삭제 중 오류 발생 가능
     // Db에 저장된 Refresh Token 삭제
    @Transactional
    public void deleteUserTokenByRefreshToken(String refreshToken)throws DataAccessException {
        UserToken token = userTokenRepository.findUserTokenByRefreshTokenOrElseThrow(refreshToken);
        userTokenRepository.delete(token);
    }
}
