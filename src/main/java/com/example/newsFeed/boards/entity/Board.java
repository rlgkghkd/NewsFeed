package com.example.newsFeed.boards.entity;

import com.example.newsFeed.boards.dto.BoardRequestDto;
import com.example.newsFeed.entity.Base;
import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "boardT")
@NoArgsConstructor
public class Board extends Base {
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String title;
    private String contents;

    //좋아요 개수
    @Setter
    private Long likesCount;

    public void update(BoardRequestDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    public Board(BoardRequestDto dto, User user) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.user = user;
    }
}
