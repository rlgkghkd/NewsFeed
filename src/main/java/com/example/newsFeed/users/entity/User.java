package com.example.newsFeed.users.entity;

import com.example.newsFeed.config.PasswordEncoder;
import com.example.newsFeed.entity.Base;
import com.example.newsFeed.users.dto.UserSignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "userT")
@NoArgsConstructor
@AllArgsConstructor
public class User extends Base {

    private String email;
    private String name;
    private String password;
    private LocalDateTime date; //생년월일
    private String introduction;
    private boolean enable;

    public static User toEntity(UserSignUpRequestDto dto){
        //암호화
        String encodedPassword = PasswordEncoder.encode(dto.getPassword());
        return new User(dto.getEmail(), dto.getName(), encodedPassword, dto.getDate().atStartOfDay(), dto.getIntroduction(), true);
    }

    public boolean checkPasswordEqual(String password){
        return PasswordEncoder.matches(password, this.password);
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updatePassword(String updatePassword){
        if(PasswordEncoder.matches(updatePassword, this.password)){
            throw new IllegalArgumentException("현재 비밀번호와는 다른 비밀번호로 변경해주세요");
        }
        this.password = PasswordEncoder.encode(updatePassword);
    }

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

    public void updateEnableFalse(){
        this.enable = false;
    }

}
