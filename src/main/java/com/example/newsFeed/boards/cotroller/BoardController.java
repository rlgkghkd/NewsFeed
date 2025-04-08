package com.example.newsFeed.boards.controller;

import com.example.newsFeed.boards.service.BoardService;
import com.example.newsFeed.boards.dto.BoardListResponseDto;
import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    /***
     * 조회단건 GET{id} ㅇ
     * 조회전체 GET ㅇ
     * 작성 POST {id} ㅇ
     * 수정 PATCH {id} ㅇ
     * 삭제 DELETE {id} ㅇ
     */

    //전체조회
    @GetMapping
    public List<BoardResponseDto> getBoardAll(){
        return boardService.getBoardAll();
    }

    //단건조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable long boardId){
        BoardResponseDto dto = boardService.getBoardById(boardId);
        return ResponseEntity.ok(dto);
    }

    //뉴스피드 조회
    @GetMapping("/page/{pageNumber}")
    public Page<BoardListResponseDto> getBoardPage(@PathVariable int pageNumber, @RequestParam(defaultValue = "10") int size)
    {
        pageNumber=5;

        return boardService.getBoardPage(pageNumber,size);
    }

    //추가
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) //JWT ID 값 필요
    {
        long userId = 1;
        BoardResponseDto dto = boardService.createBoard(boardRequestDto, userId);// service
        return ResponseEntity.ok(dto);
    }

    //수정
    //JWT userId값 수신방식에 따라서 파라미터로 할지 body로 할지, Filter 활용..?
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable long boardId, @RequestBody BoardRequestDto boardRequestDto)   //++userId
    {
        long userId = 1;
        BoardResponseDto dto = boardService.updateBoard(boardRequestDto, boardId, userId);
        return ResponseEntity.ok(dto);
    }

    
    //삭제
    //JWT userId값 수신방식에 따라서 파라미터로 할지 body로 할지, Filter 활용..?
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable long boardId) // ++ userId
    {
        long userId = 1;
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok("삭제완료");
    }
    



}
