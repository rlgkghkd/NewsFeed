package com.example.newsFeed.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignUpRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private String date;

    //공백 가능
    private String introduction;
}
