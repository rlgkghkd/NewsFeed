package com.example.newsFeed.relation.service;

import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.relation.dto.RelationshipResponseDto;
import com.example.newsFeed.relation.entity.Relationship;
import com.example.newsFeed.relation.entity.RelationshipId;
import com.example.newsFeed.relation.repository.RelationshipRepository;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    //친구요청 생성
    public RelationshipResponseDto sendRequest(String followingEmail, HttpServletRequest request) {
        String token = tokenUtils.getAccessToken(request);

        //본인
        User follower = userRepository.findById(tokenUtils.getUserIdFromToken(token)).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));
        //요청 보내는 대상
        User following = userRepository.findUserByEmailOrElseThrow(followingEmail);

        //삭제된 유저 대상으로 요청할 수 없음
        if (!following.isEnable()){throw new CustomException(Errors.USER_NOT_FOUND);}
        //본인을 대상으로 요청 생성 시 예외처리
        if (follower.equals(following)){throw new CustomException(Errors.REQUEST_TO_SELF);}
        //중복되는 요청 존재시 예외처리
        //본인과 대상이 동일한 요청, 혹은 반대인 경우를 조회.
        if (!relationshipRepository.findSpecificRelationship(follower, following).isEmpty()){throw new CustomException(Errors.REQUEST_ALREADY_EXIST);}

        //요청 생성 후 저장
        //pending은 true로 초기화
        Relationship relationship = new Relationship(new RelationshipId(follower.getId(), following.getId()), follower, following, true);
        Relationship saved = relationshipRepository.save(relationship);
        return new RelationshipResponseDto(saved);
    }

    //친구 요청 응답
    //요청 상태는 pending으로 판단. true일 시 응답이 필요한 요청, false일 시 이미 승인된 요청.
    @Transactional
    public void responseRelationship(Long followerId, boolean response, HttpServletRequest request) {
        String token = tokenUtils.getAccessToken(request);

        //요청을 보낸 유저
        User follower = userRepository.findById(followerId).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));
        //요청을 받는 대상(임시 본인)
        User following = userRepository.findById(tokenUtils.getUserIdFromToken(token)).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));


        List<Relationship> relationship = relationshipRepository.findALLByFollowerAndFollowing(follower, following).orElseThrow();
        //조회된 요청이 없을 시 예외
        if(relationship.isEmpty()){throw new CustomException(Errors.RELATION_NOT_FOUND);}
        //요청의 pending이 false일 경우 예외
        if(relationship.get(0).getPending().equals(false)){throw new CustomException(Errors.REQUEST_ALREADY_ACCEPTED);}

        //요청에 대한 응답이 true인 경우 pending을 false로 세팅
        if (response) {relationship.get(0).setPending(false);}
        //요청에 대한 응답이 false인 경우 요청을 삭제
        else { relationshipRepository.delete(relationship.get(0));}
    }

    //요청 삭제
    //승인, 미승인 된 요청 모두 삭제 가능
    public void deleteRelationship(Long otherId, HttpServletRequest request) {
        String token = tokenUtils.getAccessToken(request);

        //본인
        User me = userRepository.findById(tokenUtils.getUserIdFromToken(token)).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));
        //요청의 대상
        User other =  userRepository.findById(otherId).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));

        //본인, 대상 유저 기반으로 요청 조회
        Relationship relationship = relationshipRepository.findSpecificRelationship(me, other).get(0);
        //찾은 요청 삭제
        relationshipRepository.delete(relationship);
    }

    //유저와 관련된 모든 요청 삭제
    public void deleteAllRelationship(User user){
        List<Relationship> allRelationship = relationshipRepository.findAllByFollowerOrFollowing(user,user).orElseThrow();
        relationshipRepository.deleteAll(allRelationship);
    }


    //요청 조회
    //본인과 관련된 요청만 조회 가능.
    //전달받은 type 파라미터에 따라 다른 동작
    public List<RelationshipResponseDto> findRelationship(String type, HttpServletRequest request, int index) {
        String token = tokenUtils.getAccessToken(request);

        //본인
        User userFoundById = userRepository.findById(tokenUtils.getUserIdFromToken(token)).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(index-1, 10);

        //pending은 본인이 받은 요청중 미승인된 모든 요청 조회
        //sent는 본인이 보낸 모든 요청 조회
        //type이 없을 시 본인과 연관된 모든 요청 조회
        //기타 입력의 경우 예외처리
        Page<Relationship> foundRel = switch (type) {
            case "pending" -> relationshipRepository.findAllByFollowingAndPendingIsTrueOrElseThrow(userFoundById, pageRequest);
            case "sent" -> relationshipRepository.findAllByFollowerAndPendingIsTrueOrElseThrow(userFoundById, pageRequest);
            case "friend" -> relationshipRepository.findAllAcceptedFriends(userFoundById, pageRequest);
            case "" -> relationshipRepository.findAllRelationshipByUserOrElseThrow(userFoundById, pageRequest);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 요청 파라미터가 아닙니다.");
        };

        //조회결과가 빈 리스트일 시 예외처리
        if (foundRel.isEmpty()){throw new CustomException(Errors.RELATION_NOT_FOUND);}

        return foundRel.stream().map(RelationshipResponseDto::new).toList();
    }

    //해당 유저의 모든 친구를 User 리스트 형태로 반환
    //친구는 요청을 승인한 유저만 포함.
    public List<User> findAllFriends(User user){
        List<Relationship> relationshipList = relationshipRepository.findAllByFollowerAndPendingIsFalseOrFollowingAndPendingIsFalse(user, user).orElseThrow(()-> new CustomException(Errors.RELATION_NOT_FOUND));
        List<User> friends = new ArrayList<>();
        for (Relationship r : relationshipList){
            if(!r.getFollower().equals(user)){friends.add(r.getFollower());}
            else if(!r.getFollowing().equals(user)){friends.add(r.getFollowing());}
        }
        return friends;
    }
}
