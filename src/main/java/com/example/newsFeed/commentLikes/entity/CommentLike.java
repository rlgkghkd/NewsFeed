package com.example.newsFeed.commentLikes.entity;


import com.example.newsFeed.comment.entity.Comment;
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
@Table(name = "commentLikest")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommentLike {

    @EmbeddedId()
    CommentLikeId id;

    @MapsId("commentId")
    @ManyToOne
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
