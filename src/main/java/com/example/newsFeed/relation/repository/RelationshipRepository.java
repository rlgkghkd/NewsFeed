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
        return findAllByFollowerOrFollowing(user, user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저의 친구가 없습니다. 유저 id : " + user.getId()));
    }

    Optional<List<Relationship>> findAllByFollowingAndPendingIsTrue(User following);
    default List<Relationship> findAllByFollowingAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowingAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "처리되지 않은 요청이 없습니다."));
    }

    Optional<List<Relationship>> findAllByFollowerAndPendingIsTrue(User user);
    default List<Relationship> findAllByFollowerAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowerAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "처리되지 않은 요청이 없습니다."));
    }
}
