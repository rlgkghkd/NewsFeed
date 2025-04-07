package com.example.newsFeed.users.entity;

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
    private LocalDateTime date;
    private String introduction;
    private boolean enable;

}
