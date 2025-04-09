package com.example.newsFeed.boards.dto;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.users.entity.User;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private final String title;
    private final String contents;
    private final String userName;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.userName = board.getUser().getName();
    }
}
