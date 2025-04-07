package com.example.newsFeed.repository;

import com.example.newsFeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
}
