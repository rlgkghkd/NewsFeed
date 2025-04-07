package com.example.newsFeed.users.entity;

import com.example.newsFeed.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public void updateName(String name){
        if(this.name.equals(name)){
            throw new IllegalArgumentException("현재 이름과는 다른 이름으로 변경해주세요");
        }
        this.name = name;
    }

    //passwordEncorder 추가 필요
    public void updatePassword(String currentPassword, String updatePassword){
        if(!this.password.equals(currentPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        if(this.password.equals(updatePassword)){
            throw new IllegalArgumentException("현재 비밀번호와는 다른 비밀번호로 변경해주세요");
        }
        this.password = updatePassword;
    }

    public void updateIntroduction(String introduction){
        if(this.introduction.equals(introduction)){
            throw new IllegalArgumentException("현재 내용과는 다른 내용으로 변경해주세요");
        }
        this.introduction = introduction;
    }

}
