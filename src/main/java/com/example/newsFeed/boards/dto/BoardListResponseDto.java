package com.example.newsFeed.boards.dto;

import com.example.newsFeed.boards.entity.Board;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private final String title;
    private final String contents;
    private final String userName;

    public BoardListResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.userName = board.getUser().getName();
    }
}
