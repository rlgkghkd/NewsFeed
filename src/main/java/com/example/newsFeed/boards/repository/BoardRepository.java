package com.example.newsFeed.boards.repository;

import com.example.newsFeed.boards.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
