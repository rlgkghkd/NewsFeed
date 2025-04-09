package com.example.newsFeed.boards.service;

import com.example.newsFeed.boards.dto.BoardListResponseDto;
import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;

    public List<BoardResponseDto> getBoardAll() {
        List<Board> result = boardRepository.findAll();

        List<BoardResponseDto> dto = result.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return dto;
    }

    public BoardResponseDto getBoardById(long boardId) {
        //예외처리 하고 가져와야함
        Board board = boardRepository.getById(boardId);
        return new BoardResponseDto(board);
    }

    public Page<BoardListResponseDto> getBoardPage(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boards = boardRepository.findAll(pageable);

        return boards.map(board -> new BoardListResponseDto(board));
    }

    public BoardResponseDto createBoard(BoardRequestDto dto, long userId) //Id필요
    {
        //검증 예외처리 추가 -> 예외처리를 Entity에서 할지, Service단에서 예외를 할지??
        User user = userService.getUserById(userId);
        Board board = new Board(dto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    public BoardResponseDto updateBoard(BoardRequestDto dto, long boardId, long userId) //Id필요
    {
        //BoardId 존재여부 예외처리
        //BoardId == UserId 검증 예외처리
        Board board = boardRepository.getById(boardId); //예외처리하는 부분에서 board 리턴해주면 편함
        board.update(dto);
        return new BoardResponseDto(board);
    }

    public void deleteBoard(long boardId, long userId) {
        //BoardId 존재여부 예외처리
        //BoardId == UserId 검증 예외처리
        Board board = boardRepository.getById(boardId); //예외처리하는 부분에서 board 리턴해주면 편함
        boardRepository.delete(board);

    }

    /******************************
     * 예외처리
     ******************************/

}