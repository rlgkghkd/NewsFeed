package com.example.newsFeed.likes.service;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.likes.dto.LeaveLikeDto;
import com.example.newsFeed.likes.dto.LikeResponseDto;
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

    @Transactional
    public LikeResponseDto leaveLike(LeaveLikeDto dto, String token) {
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow();
        if(board.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}

        Like like = new Like(new LikeId(board.getId(), user.getId()),board, user);
        Like saved = likesRepository.save(like);
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
        return new LikeResponseDto(saved);
    }

    @Transactional
    public void deleteLike(Long boardId, String token){
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        Board board = boardRepository.findById(boardId).orElseThrow();
        Like target = likesRepository.findByBoardAndUser(board, user).orElseThrow();

        likesRepository.delete(target);
        board.setLikesCount((long)likesRepository.findAllByBoard(board).orElseThrow().size());
    }

    public List<LikeResponseDto> getLikes(int index, String token) {
        User user = userRepository.findByIdOrElseThrow(TokenUtils.getUserIdFromToken(token));
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        Page<Like> likePage = likesRepository.findAllByUserOrElseThrow(user, pageRequest);
        return likePage.stream().map(LikeResponseDto::new).toList();
    }
}
