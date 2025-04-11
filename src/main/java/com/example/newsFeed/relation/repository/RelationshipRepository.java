package com.example.newsFeed.relation.repository;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.relation.entity.Relationship;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {


    Optional<List<Relationship>> findAllByFollowerOrFollowing(User user, User user2);
    //유저와 연관된 모든 요청 조회
    default PageImpl<Relationship> findAllRelationshipByUserOrElseThrow(User user, PageRequest pageRequest){
        List<Relationship> searched = findAllByFollowerOrFollowing(user, user).orElseThrow(()-> new CustomException(Errors.RELATION_NOT_FOUND));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()),  searched.size());
        return new PageImpl<>(searched.subList(start, end), pageRequest, searched.size());
    }

    //모든 친구 관계 조회
    Optional<List<Relationship>> findAllByFollowerAndPendingIsFalseOrFollowingAndPendingIsFalse(User user, User user2);
    default PageImpl<Relationship> findAllAcceptedFriends(User user, PageRequest pageRequest){
        List<Relationship> searched = findAllByFollowerAndPendingIsFalseOrFollowingAndPendingIsFalse(user, user).orElseThrow(()-> new CustomException(Errors.RELATION_NOT_FOUND));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()),  searched.size());
        return new PageImpl<>(searched.subList(start, end), pageRequest, searched.size());
    }

    //요청을 보낸 유저와 받은 유저를 지정하여 조회
    Optional<List<Relationship>> findALLByFollowerAndFollowing(User user, User user2);
    //두 유저가 같이 연관된 모든 요청 조회
    default List<Relationship> findSpecificRelationship(User user, User user2){
        if(!findALLByFollowerAndFollowing(user, user2).orElseThrow().isEmpty()){return findALLByFollowerAndFollowing(user, user2).orElseThrow(()->new CustomException(Errors.RELATION_NOT_FOUND));}
        else {return findALLByFollowerAndFollowing(user2, user).orElseThrow();}
    }


    Optional<List<Relationship>> findAllByFollowingAndPendingIsTrue(User following);
    default PageImpl<Relationship> findAllByFollowingAndPendingIsTrueOrElseThrow(User user, PageRequest pageRequest){
        List<Relationship> searched =findAllByFollowingAndPendingIsTrue(user).orElseThrow(()-> new CustomException(Errors.RELATION_NOT_FOUND));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()),  searched.size());
        return new PageImpl<>(searched.subList(start, end), pageRequest, searched.size());
    }

    Optional<List<Relationship>> findAllByFollowerAndPendingIsTrue(User user);
    default PageImpl<Relationship> findAllByFollowerAndPendingIsTrueOrElseThrow(User user, PageRequest pageRequest){
        List<Relationship> searched = findAllByFollowerAndPendingIsTrue(user).orElseThrow(()-> new CustomException(Errors.RELATION_NOT_FOUND));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()),  searched.size());
        return new PageImpl<>(searched.subList(start, end), pageRequest, searched.size());
    }

    //친구 수 카운트
    @Query("SELECT COUNT(r) FROM Relationship r WHERE r.pending = false AND (r.follower = :user OR r.following = :user)")
    Long countAcceptedFriends(@Param("user") User user);

}
