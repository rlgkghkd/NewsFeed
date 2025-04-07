package com.example.newsFeed.repository;

import com.example.newsFeed.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
