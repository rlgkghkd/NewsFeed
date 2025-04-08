package com.example.newsFeed.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignUpRequestDto {
    private String email;
    private String name;
    private String password;
    private String date;
    private String introduction;
}
