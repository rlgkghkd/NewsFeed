package com.example.newsFeed.relation.controller;

import com.example.newsFeed.relation.dto.CreateRelationshipRequestDto;
import com.example.newsFeed.relation.dto.DeleteRelationshipRequestDto;
import com.example.newsFeed.relation.dto.RelationshipResponseDto;
import com.example.newsFeed.relation.dto.ResponseRelationshipRequestDto;
import com.example.newsFeed.relation.service.RelationshipService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/relationships")
public class RelationshipController {
    private final RelationshipService relationshipService;

    @PostMapping
    public RelationshipResponseDto requestRelationship(@RequestBody CreateRelationshipRequestDto dto){
        return relationshipService.sendRequest(dto.getFollowerId(), dto.getFollowingId());
    }

    @GetMapping
    public List<RelationshipResponseDto> findAllRelationship(HttpServletRequest request){
        return relationshipService.findAllRelationship(request);
    }

    @GetMapping("/pending")
    public List<RelationshipResponseDto> findAllPendingRequests(HttpServletRequest request){
        return relationshipService.findAllPendingRequests(request);
    }

    @GetMapping("/sent")
    public List<RelationshipResponseDto> findAllSentRequests(HttpServletRequest request){
        return relationshipService.findAllSentRequests(request);
    }

    @PatchMapping
    public void responseRelationshipRequest(@RequestBody ResponseRelationshipRequestDto dto){
        relationshipService.responseRelationship(dto.getId(), dto.isResponse());
    }

    @DeleteMapping
    public void deleteRelationshipRequest(@RequestBody DeleteRelationshipRequestDto dto){
        relationshipService.deleteRelationship(dto.getId());
    }
}
