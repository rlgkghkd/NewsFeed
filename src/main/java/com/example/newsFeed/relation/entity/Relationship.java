package com.example.newsFeed.relation.entity;

import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "relationship")
@NoArgsConstructor
@Setter
public class Relationship {

    @EmbeddedId
    private RelationshipId id;

    @MapsId("followerId")
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
    @MapsId("followingId")
    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    private Boolean pending;

    public Relationship(User follower, User following, Boolean pending) {
        this.follower = follower;
        this.following = following;
        this.pending = pending;
    }
}
