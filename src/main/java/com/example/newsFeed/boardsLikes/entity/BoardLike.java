package com.example.newsFeed.boardsLikes.entity;


import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "likest")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class BoardLike {

    @EmbeddedId()
    BoardLikeId id;

    @MapsId("boardId")
    @ManyToOne
    @JoinColumn(name = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
