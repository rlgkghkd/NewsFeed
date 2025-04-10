package com.example.newsFeed.comment.service;

import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.boards.service.BoardService;
import com.example.newsFeed.comment.dto.CommentRequestDto;
import com.example.newsFeed.comment.dto.CommentResponseDto;
import com.example.newsFeed.comment.entity.Comment;
import com.example.newsFeed.comment.repository.CommentRepository;
import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");


    public List<CommentResponseDto> getCommentByBoardId(long boardId) {
        //기본 정렬은 생성일자 기준으로 내림차순 정렬

        List<Comment> result = commentRepository.findByBoardId(boardId, sort);

        if (result.size() == 0) {
            throw new CustomException(Errors.SCHEDULE_NOT_FOUND); //댓글없음으로
        }

        List<CommentResponseDto> dto = result.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return dto;
    }

    public CommentResponseDto createComment(CommentRequestDto dto, long boardId, long userId) {
        User user = userService.getUserById(userId);
        Board board = boardService.checkBoardId(boardId);
        Comment Comment = new Comment(user, board, dto);
        commentRepository.save(Comment);
        return new CommentResponseDto(Comment);
    }

    public CommentResponseDto updateComment(CommentRequestDto dto, long commentId, long userId) {
        Comment comment = checkCommentId(commentId);
        checkCommentIdEqualsLoginId(comment, userId);
        comment.update(dto);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(long commentId, long userId) {
        Comment comment = checkCommentId(commentId);
        checkCommentIdEqualsLoginId(comment, userId);
        commentRepository.delete(comment);

    }

    /******************************
     * 예외처리
     ******************************/

    //CommentId 존재하는지 검사 Comment 반환
    public Comment checkCommentId(long boardId) {
        Comment comment = commentRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(Errors.SCHEDULE_NOT_FOUND));
        return comment;
    }

    //Comment의 userId와 login userId 검사
    public void checkCommentIdEqualsLoginId(Comment comment, long boardId) {
        if (!comment.getUser().getId().equals(boardId)) {
            throw new CustomException(Errors.SCHEDULE_NOT_FOUND);
        }
    }

}
