package com.example.newsFeed.users.controller;

import com.example.newsFeed.jwt.utils.TokenUtils;
import com.example.newsFeed.users.dto.*;
import com.example.newsFeed.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequestDto signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다.");
    }

    //유저 목록 페이징 조회
    @GetMapping("/page/{pageNumber}")
    public Page<UserListResponseDto> getUserPage(@PathVariable int pageNumber, @RequestParam(defaultValue = "10") int size) {
        return userService.getUserPage(pageNumber, size);
    }


    //유저 아이디 기반 단일 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.findUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    //유저 정보 변경
    @PutMapping()
    public ResponseEntity<UserResponseDto> updateUserInfo(
            @Valid @RequestBody UserUpdateRequestDto updateDto,
            HttpServletRequest request
    ) {
        String token = TokenUtils.getAccessToken(request); // 쿠키에서 accessToken 꺼내기
        Long userId = TokenUtils.getUserIdFromToken(token); // 토큰에서 userId 추출
        UserResponseDto responseDto = userService.updateUserInfo(userId, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    //유저 비밀번호 변경
    @PatchMapping()
    public ResponseEntity<String> updateUserPassword(
            @Valid @RequestBody UserPasswordRequestDto passwordDto,
            HttpServletRequest request
    ) {
        String token = TokenUtils.getAccessToken(request); // 쿠키에서 accessToken 꺼내기
        Long userId = TokenUtils.getUserIdFromToken(token); // 토큰에서 userId 추출
        userService.updateUserPassword(userId, passwordDto);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    //회원탈퇴
    @PatchMapping("/resign")
    public ResponseEntity<String> resignUser(
            @Valid @RequestBody UserDeleteRequestDto deleteDto,
            HttpServletRequest request
    ) {
        String token = TokenUtils.getAccessToken(request); // 쿠키에서 accessToken 꺼내기
        Long userId = TokenUtils.getUserIdFromToken(token); // 토큰에서 userId 추출
        userService.resignUser(userId, deleteDto);
        return ResponseEntity.ok().body("계정이 삭제되었습니다. 이용해주셔서 감사합니다.");
    }
}
