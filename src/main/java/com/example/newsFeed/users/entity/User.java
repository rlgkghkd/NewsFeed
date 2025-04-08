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

    public static User toEntity(UserSignUpRequestDto dto, LocalDateTime date){
        //암호화
        String encodedPassword = PasswordEncoder.encode(dto.getPassword());
        return new User(dto.getEmail(), dto.getName(), encodedPassword, date, dto.getIntroduction(), true);
    }

    public boolean isPasswordEqual(String password){
        return PasswordEncoder.matches(this.password, password);
    }

    public void updateName(String name){
        if(this.name.equals(name)){
            throw new IllegalArgumentException("현재 이름과는 다른 이름으로 변경해주세요");
        }
        this.name = name;
    }

    public void updatePassword(String updatePassword){
        if(PasswordEncoder.matches(this.password, updatePassword)){
            throw new IllegalArgumentException("현재 비밀번호와는 다른 비밀번호로 변경해주세요");
        }
        this.password = PasswordEncoder.encode(updatePassword);
    }

    public void updateIntroduction(String introduction){
        if(this.introduction.equals(introduction)){
            throw new IllegalArgumentException("현재 내용과는 다른 내용으로 변경해주세요");
        }
        this.introduction = introduction;
    }

}
