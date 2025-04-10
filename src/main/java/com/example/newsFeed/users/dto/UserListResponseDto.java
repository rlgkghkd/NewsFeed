package com.example.newsFeed.users.dto;

import com.example.newsFeed.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResponseDto {
    private Long id;
    private String name;

    public static UserListResponseDto toDto(User user){
        return new UserListResponseDto(user.getId(), user.getName());
    }
}
