package com.example.newsFeed.Boards.Repository;

import com.example.newsFeed.Boards.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
