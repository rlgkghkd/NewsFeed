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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final RelationshipService relationshipService;

    //생성일자 기준 정렬
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    //Board 전체 조회
    public List<BoardResponseDto> getBoardAll(){

        List<Board> result = boardRepository.findAll(sort);

        if(result.size()==0)
        {
            throw new CustomException(Errors.BOARD_NOT_FOUND);
        }

        List<BoardResponseDto> dto = result.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return dto;
    }

    //Board 단건 조회
    public BoardResponseDto getBoardById(Long boardId){
        Board board = checkBoardId(boardId);
        return new BoardResponseDto(board);
    }

    //Board 페이지 조회 조회
    public List<BoardResponseDto> getBoardPage(int page, int size)
    {
        //Page 숫자 안전 코드
        if(page < 1)
        {
            page = 1;
        }

        // 0 부터 시작하므로 -1
        page = page - 1;
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Board> boards = boardRepository.findAll(pageable);

        List<BoardResponseDto> result = boards.getContent().stream()
                .map(BoardResponseDto::new)
                .toList();

        return result;
    }

    //Upgrade 뉴스피드
    public List<BoardResponseDto> getUpgradeFeed(String sorting, LocalDate fromDate, LocalDate toDate)
    {
        List<Board> boardList = boardRepository.findAll(sort);

        if(boardList.size()==0)
        {
            throw new CustomException(Errors.BOARD_NOT_FOUND);
        }

        List<BoardResponseDto> result = boardList.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());

        //날짜 범위 지정
        if (fromDate != null){result = result.stream().filter(d->d.getModifiedAt().isAfter(fromDate.atStartOfDay())).toList();}
        if (toDate != null){result = result.stream().filter(d->d.getModifiedAt().isBefore(toDate.atStartOfDay())).toList();}

        //좋아요, 날짜 역순 정렬
        result = switch (sorting){
            case "likes"->result.stream().sorted(Comparator.comparing(BoardResponseDto::getLikesCount).reversed()).toList();
            case "dates"->result.stream().sorted(Comparator.comparing(BoardResponseDto::getModifiedAt).reversed()).toList();
            default -> result;
        };

        return result;
    }

    //친구 관계인 사람의 뉴스피드 보기
    public List<BoardResponseDto> getFollowFeedBoardAll(Long userId){
        User user = userService.getUserById(userId);
        List<User> userList =  relationshipService.findAllFriends(user);
        List<Board> result = boardRepository.findByUserInOrderByCreatedAtDesc(userList);

        if(result.size()==0)
        {
            throw new CustomException(Errors.BOARD_NOT_FOUND);
        }

        List<BoardResponseDto> dto = result.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
        return dto;

    }

    // Board 생성
    public BoardResponseDto createBoard(BoardRequestDto dto, Long userId)
    {
        User user = userService.getUserById(userId);
        Board board = new Board(dto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // Board Update 생성
    public BoardResponseDto updateBoard(BoardRequestDto dto,Long boardId, Long userId)
    {
        Board board = checkBoardId(boardId);
        checkBoardIdEqualsLoginId(board,userId);
        board.update(dto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // Board 삭제
    public void deleteBoard(Long boardId, Long userId)
    {
        Board board = checkBoardId(boardId);
        checkBoardIdEqualsLoginId(board,userId);
        boardRepository.delete(board);

    }

    /******************************
     * 예외처리
     ******************************/

    //boardId 존재하는지 검사 Board 반환
    public Board checkBoardId(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(Errors.BOARD_NOT_FOUND))
                ;
        return board;
    }

    //Board의 userId와 login userId 검사
    public void checkBoardIdEqualsLoginId(Board board, Long boardId){
        if(!board.getUser().getId().equals(boardId))
        {
            //Enum 추가해야함 "Board 의 Id와 login Id가 맞지 않는 경우"
            throw new CustomException(Errors.BOARD_NOT_FOUND);
        }
    }

}
