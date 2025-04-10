package com.example.newsFeed.boardsLikes.repository;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.boardsLikes.entity.BoardLike;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoardAndUser(Board board, User user);
    Optional<List<BoardLike>> findAllByBoard(Board board);
}
