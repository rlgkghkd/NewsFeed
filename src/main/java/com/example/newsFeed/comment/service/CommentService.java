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
import jakarta.transaction.Transactional;
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

    //생성일자 기준 정렬 Sort
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");


    //Board Id에 따른 Comment 조회
    public List<CommentResponseDto> getCommentByBoardId(Long boardId) {

        List<Comment> result = commentRepository.findByBoardId(boardId, sort);

        if (result.size() == 0) {
            throw new CustomException(Errors.COMMENT_NOT_FOUND);
        }

        List<CommentResponseDto> dto = result.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return dto;
    }

    //Comment 생성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto, Long boardId, Long userId) {
        User user = userService.getUserById(userId);
        Board board = boardService.checkBoardId(boardId);
        Comment Comment = new Comment(user, board, dto);
        commentRepository.save(Comment);
        return new CommentResponseDto(Comment);
    }

    //Comment 수정
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto dto, Long commentId, Long userId) {
        Comment comment = checkCommentId(commentId);
        checkCommentIdEqualsLoginId(comment, userId);
        comment.update(dto);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //Comment 삭제
    @Transactional
    public void deleteComment(Long commentId, Long boardId, Long userId) {
        Comment comment = checkCommentId(commentId);
        Board board = boardService.checkBoardId(boardId);
        checkBoardIdEqualsUserId(comment, board, userId);
        commentRepository.delete(comment);

    }

    /******************************
     * 예외처리
     ******************************/

    //CommentId 존재하는지 검사 Comment 반환
    public Comment checkCommentId(Long boardId) {
        Comment comment = commentRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(Errors.COMMENT_NOT_FOUND));
        return comment;
    }

    //Comment의 userId와 login userId 검사
    public void checkCommentIdEqualsLoginId(Comment comment, Long boardId) {
        if (!comment.getUser().getId().equals(boardId)) {
            throw new CustomException(Errors.UNAUTHORIZED_ACCESS);
        }
    }
    //BoardId와 UserId가 같거나, CommentId와 UserId가 같은 경우 삭제가능
    public void checkBoardIdEqualsUserId(Comment comment, Board board, Long userId){
        Long commentUserId = comment.getUser().getId();
        Long boardUserId = board.getId();

        if( !userId.equals(commentUserId) && !userId.equals(boardUserId) )
        {
            throw new CustomException(Errors.UNAUTHORIZED_ACCESS);
        }

    }

}
