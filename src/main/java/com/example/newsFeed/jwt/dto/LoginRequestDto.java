package com.example.newsFeed.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class LoginRequestDto {

   @Pattern()
   @NotBlank(message = "이메일을 필수로 입력해주세요.")
   private final String email;

   @NotBlank(message = "비밀번호를 필수로 입력해주세요")
   @Pattern()
   private final String password;

}
