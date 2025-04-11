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

    private final Long likesCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;


    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.userName = board.getUser().getName();
        this.createdAt = board.getCreatedAt();
        //좋아요 개수 추가
        this.likesCount = board.getLikesCount();
        this.modifiedAt = board.getModifiedAt();

    }
}
