package com.example.newsFeed.Users.entity;

import com.example.newsFeed.entity.Base;
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

}
