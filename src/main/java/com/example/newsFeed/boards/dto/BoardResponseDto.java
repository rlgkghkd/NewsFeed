package com.example.newsFeed.boards.dto;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private final String title;
    private final String contents;
    private final String userName;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private final Long likesCount;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.userName = board.getUser().getName();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();

        this.likesCount = board.getLikesCount();
    }
}
