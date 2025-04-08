package com.example.newsFeed.relation.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RelationshipId implements Serializable {
    private Long followerId;
    private Long followingId;
}
