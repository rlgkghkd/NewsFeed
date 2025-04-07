package com.example.newsFeed.relation.repository;

import com.example.newsFeed.relation.entity.Relationship;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Optional<List<Relationship>> findAllByFollowerOrFollowing(User user, User user2);
    default List<Relationship> findAllByFollowerOrFollowingOrElseThrow(User user){
        return findAllByFollowerOrFollowing(user, user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<List<Relationship>> findAllByFollowingAndPendingIsTrue(User following);
    default List<Relationship> findAllByFollowingAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowingAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<List<Relationship>> findAllByFollowerAndPendingIsTrue(User user);
    default List<Relationship> findAllByFollowerAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowerAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
