package com.example.newsFeed.boards.repository;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserInOrderByCreatedAtDesc(List<User> users);
}
