package com.example.newsFeed.jwt.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class TokenRedis {

    @Id
    private Long userId;

    @Column(unique = true, nullable = false)
    private String refreshToken;

    public TokenRedis (Long userId, String refreshToken){
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public static TokenRedis toEntity(Long userId, String refreshToken){

        return new TokenRedis(userId, refreshToken);
    }
}
