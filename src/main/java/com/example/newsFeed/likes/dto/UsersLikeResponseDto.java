package com.example.newsFeed.likes.dto;

import com.example.newsFeed.likes.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersLikeResponseDto {
    String boardTitle;

    public UsersLikeResponseDto(Like like) {
        this.boardTitle = like.getBoard().getTitle();
    }
}
