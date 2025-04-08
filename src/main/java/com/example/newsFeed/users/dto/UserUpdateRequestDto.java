package com.example.newsFeed.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotNull(message = "소개는 필수입니다.")
    private String introduction;
}
