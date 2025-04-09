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

    //친구요청 생성
    @PostMapping
    public RelationshipResponseDto requestRelationship(@RequestBody CreateRelationshipRequestDto dto, HttpServletRequest request) {
        return relationshipService.sendRequest(dto.getFollowingId(), request);
    }

    //친구요청 조회
    //type 파라미터는 생략 가능. 생략시 본인에게 온 모든 요청 조회
    //pending은 자신이 받은 요청중 승인 대기중인 요청 조회
    //sent는 자신이 보낸 모든 요청 조회
    @GetMapping
    public List<RelationshipResponseDto> findAllRelationship(HttpServletRequest request,
                                                             @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                                             @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        return relationshipService.findRelationship(type, request, index);
    }

    //친구 요청 승인/거부
    //요청을 보낸 user의 id 필요
    @PatchMapping
    public void responseRelationshipRequest(@RequestBody ResponseRelationshipRequestDto dto, HttpServletRequest request) {
        relationshipService.responseRelationship(dto.getFollowerId(), dto.isResponse(), request);
    }

    //친구 요청, 승인된 요청 삭제
    //요청을 자신과 요청관계에 있는 유저 id 필요
    @DeleteMapping
    public void deleteRelationshipRequest(@RequestBody DeleteRelationshipRequestDto dto, HttpServletRequest request) {
        relationshipService.deleteRelationship(dto.getOthersId(), request);
    }
}
