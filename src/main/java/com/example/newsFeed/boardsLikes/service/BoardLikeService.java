package com.example.newsFeed.boardsLikes.service;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.boardsLikes.dto.BoardLikeResponseDto;
import com.example.newsFeed.boardsLikes.entity.BoardLike;
import com.example.newsFeed.boardsLikes.entity.BoardLikeId;
import com.example.newsFeed.boardsLikes.repository.BoardLikeRepository;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository likesRepository;

    
    //게시물에 좋아요 남기기
    //좋아요 남긴 이후 게시물의 likes 필드 수정
    @Transactional
    public BoardLikeResponseDto leaveLike(Long boardId, String token) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));


        //게시물을 작성한 유저가 본인인 경우 좋아요를 남길 수 없음
        if(board.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}

        BoardLike boardLike = new BoardLike(new BoardLikeId(board.getId(), user.getId()),board, user);
        BoardLike saved = likesRepository.save(boardLike);
        
        //게시물 객체의 좋아요 필드 수정.
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
        return new BoardLikeResponseDto(saved);
    }

    
    //좋아요 삭제
    @Transactional
    public void deleteLike(Long boardId, String token){
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Board board = boardRepository.findById(boardId).orElseThrow();
        BoardLike target = likesRepository.findByBoardAndUser(board, user).orElseThrow();

        likesRepository.delete(target);
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
    }
}
