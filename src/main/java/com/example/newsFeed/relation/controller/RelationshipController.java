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
    public List<RelationshipResponseDto> findRelationship(HttpServletRequest request){
        return relationshipService.findRelationship(request);
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
