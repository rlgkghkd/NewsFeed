package com.example.newsFeed.Users.Repository;

import com.example.newsFeed.Users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
}
