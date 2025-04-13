package com.example.newsFeed.jwt.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "usertokenT")
public class UserToken {

    @Id
    private Long userId;

    @Column(unique = true, nullable = false)
    private String refreshToken;

    public UserToken(Long userId, String refreshToken){
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public static UserToken toEntity(Long userId, String refreshToken){

        return new UserToken(userId, refreshToken);
    }
}
