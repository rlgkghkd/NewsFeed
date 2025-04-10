package com.example.newsFeed.commentLikes.dto;

import com.example.newsFeed.commentLikes.entity.CommentLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeResponseDto {
    Long commentId;
    String userName;

    public CommentLikeResponseDto(CommentLike commentLike) {
        this.commentId = commentLike.getComment().getId();
        this.userName = commentLike.getUser().getName();
    }
}
