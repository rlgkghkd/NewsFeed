package com.example.newsFeed.relation.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipId implements Serializable {
    private Long followerId;
    private Long followingId;
}
