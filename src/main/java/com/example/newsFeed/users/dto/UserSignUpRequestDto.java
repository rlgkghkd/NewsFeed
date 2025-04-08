package com.example.newsFeed.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserSignUpRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 주소를 입력하세요.")
    private final String email;

    @NotBlank(message = "이름은 필수입니다.")
    private final String name;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private final String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "생년월일은 현재보다 과거여야 합니다.")
    private final LocalDate date;

    //공백 가능
    private final String introduction;
}
