package com.example.newsFeed.relation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CreateRelationshipRequestDto {
    private Long followerId;
    private Long followingId;
}
