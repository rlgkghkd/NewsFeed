package com.example.newsFeed.relation.service;

import com.example.newsFeed.relation.dto.RelationshipResponseDto;
import com.example.newsFeed.relation.entity.Relationship;
import com.example.newsFeed.relation.entity.RelationshipId;
import com.example.newsFeed.relation.repository.RelationshipRepository;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    public RelationshipResponseDto sendRequest(Long targetId, HttpServletRequest request) {
        request.getHeader("Auto");
        String name = "userName got from requestHeader";

        User follower = userRepository.findById((long)1).orElseThrow();
        User following = userRepository.findById(targetId).orElseThrow();

        if (!relationshipRepository.findSpecificRelationship(follower, following).isEmpty()){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 요청입니다.");}

        Relationship relationship = new Relationship(new RelationshipId(follower.getId(), following.getId()), follower, following, true);
        Relationship saved = relationshipRepository.save(relationship);
        return new RelationshipResponseDto(saved);
    }

    @Transactional
    public void responseRelationship(Long followerId, boolean response, HttpServletRequest request) {
        request.getHeader("Auto");
        String name = "userName got from requestHeader";

        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById((long)1).orElseThrow();
        List<Relationship> relationship = relationshipRepository.findALLByFollowerAndFollowing(follower, following).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."));
        if(relationship.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 요청입니다.");}
        if(relationship.get(0).getPending().equals(false)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 요청입니다.");}
        if (response) {
            relationship.get(0).setPending(false);
        } else {
            relationshipRepository.delete(relationship.get(0));
        }
    }

    @Transactional
    public void deleteRelationship(Long otherId, HttpServletRequest request) {
        User me = userRepository.findById((long)1).orElseThrow();
        User other =  userRepository.findById(otherId).orElseThrow();
        Relationship relationship = relationshipRepository.findSpecificRelationship(me, other).get(0);
        relationshipRepository.delete(relationship);
    }

    public List<RelationshipResponseDto> findRelationship(HttpServletRequest request, String type) {
        request.getHeader("Auto");
        String name = "userName got from requestHeader";
        User userFoundById = userRepository.findById((long) 1).orElseThrow();

        List<Relationship> foundRel = switch (type) {
            case "pending" -> relationshipRepository.findAllByFollowingAndPendingIsTrueOrElseThrow(userFoundById);
            case "sent" -> relationshipRepository.findAllByFollowerAndPendingIsTrueOrElseThrow(userFoundById);
            case "" -> relationshipRepository.findAllRelationshipByUserOrElseThrow(userFoundById);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 요청 파라미터가 아닙니다.");
        };

        if (foundRel.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 결과가 없습니다.");}
        return foundRel.stream().map(RelationshipResponseDto::new).toList();
    }
}
