package com.example.newsFeed.likes.controller;

import com.example.newsFeed.likes.dto.DeleteLikeDto;
import com.example.newsFeed.likes.dto.LeaveLikeDto;
import com.example.newsFeed.likes.dto.LikeResponseDto;
import com.example.newsFeed.likes.entity.Like;
import com.example.newsFeed.likes.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likesService;

    //board 에 좋아요 남기기
    //본인 소유 board 에는 좋아요 불가능
    @PostMapping
    public LikeResponseDto leaveLike(@CookieValue(name = "accessToken") String token, @RequestBody LeaveLikeDto dto){
        return likesService.leaveLike(dto, token);
    }

    //board 에 남긴 좋아요 지우기
    //board 의 아이디를 받음에 주의
    @DeleteMapping
    public void deleteLike(@RequestBody DeleteLikeDto dto, @CookieValue(name = "accessToken") String token){
        likesService.deleteLike(dto.getBoardId(), token);
    }

    @GetMapping
    public List<LikeResponseDto> getLikes(
            @RequestParam(name = "index", required = false, defaultValue = "1") int index,
            @CookieValue(name = "accessToken") String token){
        return likesService.getLikes(index, token);
    }
}
