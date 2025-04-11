package com.example.newsFeed.commentLikes.repository;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.comment.entity.Comment;
import com.example.newsFeed.commentLikes.entity.CommentLike;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
    Optional<List<CommentLike>> findAllByComment(Comment comment);
}
