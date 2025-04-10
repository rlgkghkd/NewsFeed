package com.example.newsFeed.likes.dto;

import com.example.newsFeed.likes.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {
    String boardTitle;
    String userName;

    public LikeResponseDto(Like like) {
        this.boardTitle = like.getBoard().getTitle();
        this.userName = like.getUser().getName();
    }
}
