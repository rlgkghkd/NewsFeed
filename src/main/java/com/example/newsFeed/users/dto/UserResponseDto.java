package com.example.newsFeed.users.dto;

import com.example.newsFeed.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String name;        //이름
    private String introduction;//소개
    //private Long boardsCount;        //작성한 게시글 수
    //private Long friendsCount;       //친구 수
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getName(), user.getIntroduction());
    }
}
