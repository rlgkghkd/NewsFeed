package com.example.newsFeed.users.Repository;

import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
}
