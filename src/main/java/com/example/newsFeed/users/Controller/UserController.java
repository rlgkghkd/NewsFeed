package com.example.newsFeed.users.Controller;

import com.example.newsFeed.users.dto.UserPasswordRequestDto;
import com.example.newsFeed.users.dto.UserSignUpRequestDto;
import com.example.newsFeed.users.dto.UserUpdateRequestDto;
import com.example.newsFeed.users.dto.UserResponseDto;
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
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserSignUpRequestDto signUpDto){
        userService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //유저 아이디 기반 단일 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.findUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    //Token에 저장되어 있는 유저 아이디를 가져와야 하지만
    //일단 동작을 위해서 API 엔드포인트로 Id를 받아옴
    @PutMapping("'/{id}")
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
    public ResponseEntity<Void> updateUserPassword(
            @PathVariable Long id,
            @RequestBody UserPasswordRequestDto passwordDto,
            HttpServletRequest request
    ) {
        userService.updateUserPassword(id, passwordDto);
        return ResponseEntity.ok().build();
    }
}
