package com.example.newsFeed.comment.dto;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final String contents;
    private final String userName;

    public CommentResponseDto(Comment comment) {
        this.contents = comment.getContents();
        this.userName = comment.getUser().getName();
    }
}
