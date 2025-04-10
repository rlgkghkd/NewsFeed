package com.example.newsFeed.likes.entity;


import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Like {

    @EmbeddedId()
    LikeId id;

    @MapsId("boardId")
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
