package com.example.newsFeed.comment.entity;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.comment.dto.CommentRequestDto;
import com.example.newsFeed.entity.Base;
import com.example.newsFeed.users.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "commentT")
@NoArgsConstructor
public class Comment extends Base {
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    private String contents;

    public void update(CommentRequestDto dto) {
        this.contents = dto.getContents();
    }

    public Comment(User user, Board board, CommentRequestDto dto) {
        this.user = user;
        this.board = board;
        this.contents = dto.getContents();
    }
}
