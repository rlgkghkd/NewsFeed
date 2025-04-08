package com.example.newsFeed.users.repository;

import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다." + id));
    }

    boolean existsByEmail(String email);
}
