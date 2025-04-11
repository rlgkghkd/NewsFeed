package com.example.newsFeed.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogoutResponseDto {

    //필드가 1개지만 확장성 때문에 dto로 만들어 두었습니다.
    private final String logoutMessage;
}
