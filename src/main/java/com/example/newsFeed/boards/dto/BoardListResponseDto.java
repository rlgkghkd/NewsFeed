package com.example.newsFeed.boards.dto;

import com.example.newsFeed.boards.entity.Board;
import lombok.Getter;

@Getter
public class BoardListResponseDto {
    private final String title;
    private final String contents;
    //userName필요함

    public BoardListResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
//        userName필요함
    }
}
