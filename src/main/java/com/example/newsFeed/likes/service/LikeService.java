package com.example.newsFeed.likes.service;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.likes.dto.LeaveLikeDto;
import com.example.newsFeed.likes.dto.LikeResponseDto;
import com.example.newsFeed.likes.dto.UsersLikeResponseDto;
import com.example.newsFeed.likes.entity.Like;
import com.example.newsFeed.likes.entity.LikeId;
import com.example.newsFeed.likes.repository.LikeRepository;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likesRepository;

    
    //게시물에 좋아요 남기기
    //좋아요 남긴 이후 게시물의 likes 필드 수정
    @Transactional
    public LikeResponseDto leaveLike(LeaveLikeDto dto, String token) {
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow();
        //게시물을 작성한 유저가 본인인 경우 좋아요를 남길 수 없음
        if(board.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}

        Like like = new Like(new LikeId(board.getId(), user.getId()),board, user);
        Like saved = likesRepository.save(like);
        
        //게시물 객체의 좋아요 필드 수정.
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
        return new LikeResponseDto(saved);
    }

    
    //좋아요 삭제
    @Transactional
    public void deleteLike(Long boardId, String token){
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Board board = boardRepository.findById(boardId).orElseThrow();
        Like target = likesRepository.findByBoardAndUser(board, user).orElseThrow();

        likesRepository.delete(target);
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
    }

    //좋아요 조회
    public List<UsersLikeResponseDto> getLikes(int boardId, int index, String token) {
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        Page<Like> likePage = likesRepository.findAllByUserOrElseThrow(user, pageRequest);
        return likePage.stream().map(UsersLikeResponseDto::new).toList();
    }
}
