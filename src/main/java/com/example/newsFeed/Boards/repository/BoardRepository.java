package com.example.newsFeed.Boards.repository;

import com.example.newsFeed.Boards.dto.BoardListResponseDto;
import com.example.newsFeed.Boards.entity.Board;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
