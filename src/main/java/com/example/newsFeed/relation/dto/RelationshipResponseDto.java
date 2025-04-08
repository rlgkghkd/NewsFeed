package com.example.newsFeed.relation.dto;

import com.example.newsFeed.relation.entity.Relationship;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelationshipResponseDto {
    private Long id;
    private String followerName;
    private String followingName;
    private Boolean pending;

    public RelationshipResponseDto(Relationship relationship) {
        this.followerName = relationship.getFollower().getName();
        this.followingName = relationship.getFollowing().getName();
        this.pending = relationship.getPending();
    }
}
