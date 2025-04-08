package com.example.newsFeed.users.controller;

import com.example.newsFeed.users.dto.*;
import com.example.newsFeed.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping()
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto signUpDto){
        userService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다.");
    }

    //유저 아이디 기반 단일 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.findUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    //Token에 저장되어 있는 유저 아이디를 가져와야 하지만
    //일단 동작을 위해서 API 엔드포인트로 Id를 받아옴
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserInfo(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto updateDto,
            HttpServletRequest request
    ){
        UserResponseDto responseDto = userService.updateUserInfo(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    //비밀번호 수정
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUserPassword(
            @PathVariable Long id,
            @RequestBody UserPasswordRequestDto passwordDto,
            HttpServletRequest request
    ) {
        userService.updateUserPassword(id, passwordDto);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    //회원탈퇴
    @PatchMapping("/resign")
    public ResponseEntity<String> resignUser(@RequestBody UserDeleteRequestDto deleteDto){
        Long id = 5L;
        userService.resignUser(id, deleteDto);
        return ResponseEntity.ok().body("계정이 삭제되었습니다. 이용해주셔서 감사합니다.");
    }
}