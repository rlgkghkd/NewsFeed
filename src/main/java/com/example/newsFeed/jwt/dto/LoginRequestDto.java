package com.example.newsFeed.jwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {

   @NotBlank(message = "이메일을 필수로 입력해주세요.")
   @Email(message = "올바른 이메일 주소를 입력하세요.")
   private final String email;

   @NotBlank(message = "비밀번호를 필수로 입력해주세요")
   private final String password;

}
