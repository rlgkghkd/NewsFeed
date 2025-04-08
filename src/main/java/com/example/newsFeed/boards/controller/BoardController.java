package com.example.newsFeed.boards.controller;

import com.example.newsFeed.boards.service.BoardService;
import com.example.newsFeed.boards.dto.BoardListResponseDto;
import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.jwt.TokenUtils;
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
    private final TokenUtils tokenUtils;

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
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                                        @CookieValue(name = "accessToken", required = false) String token)
    {
        Long userId = tokenUtils.getUserIdFromToken(token);
        BoardResponseDto dto = boardService.createBoard(boardRequestDto, userId);
        return ResponseEntity.ok(dto);
    }

    //수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable long boardId, @RequestBody BoardRequestDto boardRequestDto
            , @CookieValue(name = "accessToken", required = false) String token)
    {
        Long userId = tokenUtils.getUserIdFromToken(token);
        BoardResponseDto dto = boardService.updateBoard(boardRequestDto, boardId, userId);
        return ResponseEntity.ok(dto);
    }

    //삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable long boardId
            , @CookieValue(name = "accessToken", required = false) String token)
    {
        Long userId = tokenUtils.getUserIdFromToken(token);
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok("삭제완료");
    }
    



}
