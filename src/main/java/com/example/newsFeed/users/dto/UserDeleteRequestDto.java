package com.example.newsFeed.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$",
            message = "허용되지 않는 형식입니다."
    )
    private final String password;
}
