package com.example.newsFeed.relation.dto;

import com.example.newsFeed.relation.entity.Relationship;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelationshipResponseDto {
    private String followerName;
    private String followingName;
    private String status;

    public RelationshipResponseDto(Relationship relationship) {
        this.followerName = relationship.getFollower().getName();
        this.followingName = relationship.getFollowing().getName();
        this.status = relationship.getPending()?"수락 대기중":"친구";
    }
}
