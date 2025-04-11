package com.example.newsFeed.commentLikes.controller;

import com.example.newsFeed.commentLikes.dto.CommentLikeResponseDto;
import com.example.newsFeed.commentLikes.service.CommentLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/{commentId}/likes")
public class CommentLikeController {

    private final CommentLikeService likesService;

    //comment 에 좋아요 남기기
    //본인 소유 comment 에는 좋아요 불가능
    @PostMapping
    public CommentLikeResponseDto leaveLike(HttpServletRequest request, @PathVariable(name = "commentId") Long commentId){
        return likesService.leaveLike(commentId, request);
    }

    //comment 에 남긴 좋아요 지우기
    //comment 의 아이디를 받음에 주의
    @DeleteMapping
    public void deleteLike(HttpServletRequest request, @PathVariable(name = "commentId") Long commentId){
        likesService.deleteLike(commentId, request);
    }
}
