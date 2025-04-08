package com.example.newsFeed.relation.entity;

import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "relationship")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Relationship {

    @EmbeddedId
    private RelationshipId id;

    @MapsId("followerId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "follower_id")
    private User follower;
    @MapsId("followingId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "following_id")
    private User following;

    private Boolean pending;
}
