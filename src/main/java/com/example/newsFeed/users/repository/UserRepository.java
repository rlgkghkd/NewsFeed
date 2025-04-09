package com.example.newsFeed.users.repository;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(Errors.USER_NOT_FOUND));
    }

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    default User findUserByEmailOrElseThrow(String email) {
        return findUserByEmail(email).orElseThrow(() -> new CustomException(Errors.USER_NOT_FOUND));
    }
}
