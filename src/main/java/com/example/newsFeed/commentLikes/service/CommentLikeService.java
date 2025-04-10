package com.example.newsFeed.commentLikes.service;

import com.example.newsFeed.comment.entity.Comment;
import com.example.newsFeed.comment.repository.CommentRepository;
import com.example.newsFeed.commentLikes.dto.CommentLikeResponseDto;
import com.example.newsFeed.commentLikes.entity.CommentLike;
import com.example.newsFeed.commentLikes.entity.CommentLikeId;
import com.example.newsFeed.commentLikes.repository.CommentLikeRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;


    //게시물에 좋아요 남기기
    //좋아요 남긴 이후 게시물의 likes 필드 수정
    @Transactional
    public CommentLikeResponseDto leaveLike(Long commentId, String token) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));


        //게시물을 작성한 유저가 본인인 경우 좋아요를 남길 수 없음
        if(comment.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}

        CommentLike commentLike = new CommentLike(new CommentLikeId(comment.getId(), user.getId()),comment, user);
        CommentLike saved = commentLikeRepository.save(commentLike);
        
        //게시물 객체의 좋아요 필드 수정.
        comment.setLikes((long)commentLikeRepository.findAllByComment(comment).orElseThrow().size());
        return new CommentLikeResponseDto(saved);
    }

    
    //좋아요 삭제
    @Transactional
    public void deleteLike(Long boardId, String token){
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Comment comment = commentRepository.findById(boardId).orElseThrow();
        CommentLike target = commentLikeRepository.findByCommentAndUser(comment, user).orElseThrow();

        commentLikeRepository.delete(target);
        comment.setLikes((long)commentLikeRepository.findAllByComment(comment).orElseThrow().size());
    }
}
