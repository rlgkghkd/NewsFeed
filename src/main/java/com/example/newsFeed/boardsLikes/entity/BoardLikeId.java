package com.example.newsFeed.boardsLikes.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardLikeId implements Serializable {
    private Long boardId;
    private Long userId;
}
