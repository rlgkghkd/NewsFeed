package com.example.newsFeed.jwt.repository;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    //refreshToken으로 UserToken을 조회
    Optional<UserToken> findByRefreshToken(String refreshToken);

    //refreshToken으로 UserToken을 조회하고,존재하지 않으면 CustomException(Errors.USER_TOKEN_NOT_FOUND) 예외 발생
    default UserToken findUserTokenByRefreshTokenOrElseThrow(String refreshToken) {
        return findByRefreshToken(refreshToken).orElseThrow(()-> new CustomException(Errors.USER_TOKEN_NOT_FOUND));
    }
}
