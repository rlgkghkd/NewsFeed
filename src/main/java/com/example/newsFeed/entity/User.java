package com.example.newsFeed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "userT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Base {

    private String email;
    private String name;
    private String password;
    private LocalDateTime date;
    private String introduction;
    private boolean enable;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> freinds =  new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> freindRequest =  new ArrayList<>();

}
