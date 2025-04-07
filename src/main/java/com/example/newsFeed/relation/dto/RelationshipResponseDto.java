package com.example.newsFeed.relation.dto;

import com.example.newsFeed.relation.entity.Relationship;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class RelationshipResponseDto {
    private Long id;
    private String followerName;
    private String followingName;

    public RelationshipResponseDto(Relationship relationship) {
        this.id = relationship.getId();
        this.followerName = relationship.getFollower().getName();
        this.followingName = relationship.getFollowing().getName();
    }
}
