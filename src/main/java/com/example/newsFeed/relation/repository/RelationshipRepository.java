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
    //유저와 연관된 모든 요청 조회
    default List<Relationship> findAllRelationshipByUserOrElseThrow(User user){
        return findAllByFollowerOrFollowing(user, user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //모든 친구 관계 조회
    Optional<List<Relationship>> findAllByFollowerOrFollowingAndPendingIsFalse(User user, User user2);
    default List<Relationship> findAllByFollowerOrFollowingAndPendingIsFalseOrElseThrow(User user){
        return findAllByFollowerOrFollowing(user, user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //요청을 보낸 유저와 받은 유저를 지정하여 조회
    Optional<List<Relationship>> findALLByFollowerAndFollowing(User user, User user2);
    //두 유저가 같이 연관된 모든 요청 조회
    default List<Relationship> findSpecificRelationship(User user, User user2){
        if(!findALLByFollowerAndFollowing(user, user2).orElseThrow().isEmpty()){return findALLByFollowerAndFollowing(user, user2).orElseThrow();}
        else {return findALLByFollowerAndFollowing(user2, user).orElseThrow();}
    }


    Optional<List<Relationship>> findAllByFollowingAndPendingIsTrue(User following);
    //
    default List<Relationship> findAllByFollowingAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowingAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<List<Relationship>> findAllByFollowerAndPendingIsTrue(User user);
    default List<Relationship> findAllByFollowerAndPendingIsTrueOrElseThrow(User user){
        return findAllByFollowerAndPendingIsTrue(user).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
