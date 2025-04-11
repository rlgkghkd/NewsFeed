package com.example.newsFeed.jwt.repository;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByRefreshToken(String refreshToken);

    default UserToken findUserTokenByRefreshTokenOrElseThrow(String refreshToken) {
        return findByRefreshToken(refreshToken).orElseThrow(()-> new CustomException(Errors.USER_TOKEN_NOT_FOUND));
    }

    void deleteByRefreshToken(String refreshToken);
}
