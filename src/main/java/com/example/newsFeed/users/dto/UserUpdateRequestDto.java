package com.example.newsFeed.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String introduction;
    private String currentPassword;
    private String updatePassword;
}
