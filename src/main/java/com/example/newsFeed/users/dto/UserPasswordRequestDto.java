package com.example.newsFeed.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordRequestDto {
    @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String currentPassword;
    @NotBlank(message = "변경할 비밀번호 입력은 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String updatePassword;
}
