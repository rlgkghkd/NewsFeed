package com.example.newsFeed.boards.dto;

import com.example.newsFeed.boards.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private final String title;
    private final String contents;
    private final String userName;
    
    //정렬용 데이터 추가
    private final Long likesCount;
    private final LocalDateTime modifiedAt;

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.userName = board.getUser().getName();
        
        //좋아요 개수 추가
        this.likesCount = board.getLikesCount();
        this.modifiedAt = board.getModifiedAt();
    }
}
