package com.example.newsFeed.relation.service;

import com.example.newsFeed.relation.dto.RelationshipResponseDto;
import com.example.newsFeed.relation.entity.Relationship;
import com.example.newsFeed.relation.repository.RelationshipRepository;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    public RelationshipResponseDto sendRequest(Long targetId, HttpServletRequest request) {
        request.getHeader("Auto");
        String name = "userName got from requestHeader";

        User follower = userRepository.findById((long)1).orElseThrow();
        User following = userRepository.findById(targetId).orElseThrow();

        Relationship relationship = new Relationship(follower, following, true);
        Relationship saved = relationshipRepository.save(relationship);
        return new RelationshipResponseDto(saved);
    }

    @Transactional
    public void responseRelationship(Long id, boolean response) {
        Relationship relationship = relationshipRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id를 가진 유저가 없음. id : " + id));
        if(relationship.getPending().equals(false)){throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 요청입니다.");}
        if (response) {
            relationship.setPending(false);
        } else {
            relationshipRepository.delete(relationship);
        }
    }

    @Transactional
    public void deleteRelationship(Long id) {
        Relationship relationship = relationshipRepository.findById(id).orElseThrow();
        relationshipRepository.delete(relationship);
    }

    public List<RelationshipResponseDto> findRelationship(HttpServletRequest request, String type) {
        request.getHeader("Auto");
        String name = "userName got from requestHeader";
        User userFoundById = userRepository.findById((long) 1).orElseThrow();

        List<Relationship> foundRel = switch (type) {
            case "pending" -> relationshipRepository.findAllByFollowingAndPendingIsTrueOrElseThrow(userFoundById);
            case "sent" -> relationshipRepository.findAllByFollowerAndPendingIsTrueOrElseThrow(userFoundById);
            case "" -> relationshipRepository.findAllByFollowerOrFollowingOrElseThrow(userFoundById);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 요청 파라미터가 아닙니다.");
        };

        if (foundRel.isEmpty()){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 결과가 없습니다.");}
        return foundRel.stream().map(RelationshipResponseDto::new).toList();
    }
}
