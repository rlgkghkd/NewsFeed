package com.example.newsFeed.comment.controller;

import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.comment.dto.CommentRequestDto;
import com.example.newsFeed.comment.dto.CommentResponseDto;
import com.example.newsFeed.comment.service.CommentService;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {
    private final CommentService commentService;

    //BoardId에 따른 Comment 조회
    @GetMapping
    public List<CommentResponseDto> getCommentByBoardId(@PathVariable Long boardId) {
        List<CommentResponseDto> dto = commentService.getCommentByBoardId(boardId);
        return dto;
    }

    //Comment 추가
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        CommentResponseDto dto = commentService.createComment(commentRequestDto, boardId, userId);
        return ResponseEntity.ok(dto);
    }

    //Comment 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        System.out.println(commentId);
        CommentResponseDto dto = commentService.updateComment(commentRequestDto, commentId, userId);
        return ResponseEntity.ok(dto);
    }

    //Comment 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("삭제완료");
    }


}
