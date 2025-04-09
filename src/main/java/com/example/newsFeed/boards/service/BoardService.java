package com.example.newsFeed.boards.service;

import com.example.newsFeed.boards.dto.BoardListResponseDto;
import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.relation.service.RelationshipService;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final RelationshipService relationshipService;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public List<BoardResponseDto> getBoardAll(){
        //기본 정렬은 생성일자 기준으로 내림차순 정렬

        List<Board> result = boardRepository.findAll(sort);

        if(result.size()==0)
        {
            throw new CustomException(Errors.SCHEDULE_NOT_FOUND);
        }

        List<BoardResponseDto> dto = result.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return dto;
    }

    public BoardResponseDto getBoardById(long boardId){
        Board board = checkBoardId(boardId);
        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> getBoardPage(int page, int size, String sorting)
    {
        if(page < 1)
        {
            page = 1;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Board> boards = boardRepository.findAll(pageable);

        List<BoardResponseDto> result = boards.getContent().stream()
                .map(BoardResponseDto::new)
                .toList();


        result = switch (sorting){
            case "likes"->result.stream().sorted(Comparator.comparing(BoardResponseDto::getLikesCount).reversed()).toList();
            case "dates"->result.stream().sorted(Comparator.comparing(BoardResponseDto::getModifiedAt).reversed()).toList();
            default -> result;
        };

        return result;
    }

    public List<BoardResponseDto> getFollowFeedBoardAll(long userId){
        User user = userService.getUserById(userId);
        List<User> userList =  relationshipService.findAllFriends(user);
        List<Board> result = boardRepository.findByUserInOrderByCreatedAtDesc(userList);

        if(result.size()==0)
        {
            throw new CustomException(Errors.SCHEDULE_NOT_FOUND);
        }

        List<BoardResponseDto> dto = result.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return dto;

    }

    public BoardResponseDto createBoard(BoardRequestDto dto, long userId)
    {
        User user = userService.getUserById(userId);
        Board board = new Board(dto, user);
        board.setLikesCount((long)0);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    public BoardResponseDto updateBoard(BoardRequestDto dto,long boardId, long userId)
    {
        Board board = checkBoardId(boardId);
        checkBoardIdEqualsLoginId(board,userId);
        board.update(dto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }
    
    public void deleteBoard(long boardId, long userId)
    {
        Board board = checkBoardId(boardId);
        checkBoardIdEqualsLoginId(board,userId);
        boardRepository.delete(board);

    }

    /******************************
     * 예외처리
     ******************************/

    //boardId 존재하는지 검사 Board 반환
    public Board checkBoardId(long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(Errors.SCHEDULE_NOT_FOUND))
                ;
        return board;
    }

    //Board의 userId와 login userId 검사
    public void checkBoardIdEqualsLoginId(Board board, long boardId){
        if(!board.getUser().getId().equals(boardId))
        {
            //Enum 추가해야함 "Board 의 Id와 login Id가 맞지 않는 경우"
            throw new CustomException(Errors.SCHEDULE_NOT_FOUND);
        }
    }








}
