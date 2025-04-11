package com.example.newsFeed.relation.entity;

import com.example.newsFeed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "relationshipt")
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Relationship {

    @EmbeddedId
    private RelationshipId id;

    @MapsId("followerId")
    @ManyToOne()
    @JoinColumn(name = "follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User follower;

    @MapsId("followingId")
    @ManyToOne()
    @JoinColumn(name = "following_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User following;

    private Boolean pending;
}
