package com.example.newsFeed.Users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String name;        //이름
    private String introduction;//소개
    //작성한 게시글 수
    //친구 수
}
