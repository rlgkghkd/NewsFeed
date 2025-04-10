package com.example.newsFeed.commentLikes.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeId implements Serializable {
    private Long commentId;
    private Long userId;
}
