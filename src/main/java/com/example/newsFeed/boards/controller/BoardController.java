package com.example.newsFeed.boards.controller;

import com.example.newsFeed.boards.service.BoardService;
import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.boards.dto.BoardResponseDto;
import com.example.newsFeed.jwt.utils.TokenUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public List<BoardResponseDto> getBoardAll() {
        return boardService.getBoardAll();
    }

    //단건조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable long boardId) {
        BoardResponseDto dto = boardService.getBoardById(boardId);
        return ResponseEntity.ok(dto);
    }

    //뉴스피드 조회
    @GetMapping("/page/{pageNumber}")
    public List<BoardResponseDto> getBoardPage(@PathVariable int pageNumber, @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardPage(pageNumber, size);
    }

    //findAllFriends
    @GetMapping("/followFeed")
    public List<BoardResponseDto> getFollowFeedBoardAll(HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        return boardService.getFollowFeedBoardAll(userId);
    }

    //추가
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        BoardResponseDto dto = boardService.createBoard(boardRequestDto, userId);
        return ResponseEntity.ok(dto);
    }

    //수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable long boardId, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        BoardResponseDto dto = boardService.updateBoard(boardRequestDto, boardId, userId);
        return ResponseEntity.ok(dto);
    }

    //삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable long boardId, HttpServletRequest request) {
        String token = TokenUtils.getAccessToken(request);
        Long userId = TokenUtils.getUserIdFromToken(token);
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.ok("삭제완료");
    }


}
