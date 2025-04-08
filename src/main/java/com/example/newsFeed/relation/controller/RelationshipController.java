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
    public RelationshipResponseDto requestRelationship(@RequestBody CreateRelationshipRequestDto dto, HttpServletRequest request){
        return relationshipService.sendRequest(dto.getFollowingId(), request);
    }

    @GetMapping
    public List<RelationshipResponseDto> findAllRelationship(HttpServletRequest request, @RequestParam(value = "type", required = false, defaultValue = "") String type){
        return relationshipService.findRelationship(request, type);
    }

    @PatchMapping
    public void responseRelationshipRequest(@RequestBody ResponseRelationshipRequestDto dto, HttpServletRequest request){
        relationshipService.responseRelationship(dto.getFollowerId(), dto.isResponse(), request);
    }

    @DeleteMapping
    public void deleteRelationshipRequest(@RequestBody DeleteRelationshipRequestDto dto){
        relationshipService.deleteRelationship(dto.getRelationshipId());
    }
}
