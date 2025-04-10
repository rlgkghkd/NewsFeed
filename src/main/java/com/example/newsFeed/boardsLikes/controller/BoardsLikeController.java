package com.example.newsFeed.boardsLikes.controller;

import com.example.newsFeed.boardsLikes.dto.BoardLikeResponseDto;
import com.example.newsFeed.boardsLikes.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardsId}/likes")
public class BoardsLikeController {

    private final BoardLikeService likesService;

    //board 에 좋아요 남기기
    //본인 소유 board 에는 좋아요 불가능
    @PostMapping
    public BoardLikeResponseDto leaveLike(@CookieValue(name = "accessToken") String token, @PathVariable Long boardsId){
        return likesService.leaveLike(boardsId, token);
    }

    //board 에 남긴 좋아요 지우기
    @DeleteMapping
    public void deleteLike(@CookieValue(name = "accessToken") String token, @PathVariable Long boardsId){
        likesService.deleteLike(boardsId, token);
    }
}
