package com.example.newsFeed.users.dto;

import com.example.newsFeed.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;        //이름
    private String introduction;//소개
    private Long boards;        //작성한 게시글 수
    private Long friends;       //친구 수
    public static UserResponseDto toDto(User user, Long boards, Long friends) {
        return new UserResponseDto(user.getId(), user.getName(), user.getIntroduction(), boards, friends);
    }
}
