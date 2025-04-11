package com.example.newsFeed.comment.repository;

import com.example.newsFeed.comment.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId, Sort sort);

}
