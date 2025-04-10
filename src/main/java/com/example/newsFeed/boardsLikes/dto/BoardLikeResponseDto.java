package com.example.newsFeed.boardsLikes.dto;

import com.example.newsFeed.boardsLikes.entity.BoardLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardLikeResponseDto {
    String boardTitle;
    String userName;

    public BoardLikeResponseDto(BoardLike boardLike) {
        this.boardTitle = boardLike.getBoard().getTitle();
        this.userName = boardLike.getUser().getName();
    }
}
