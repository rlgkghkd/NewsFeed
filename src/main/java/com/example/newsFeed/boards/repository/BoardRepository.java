package com.example.newsFeed.boards.repository;

import com.example.newsFeed.boards.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //유저 아이디에 따른 게시물 개수 반환
    Long countByUser_Id(Long userId);
}
