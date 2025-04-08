package com.example.newsFeed.users.controller;

<<<<<<< HEAD
import com.example.newsFeed.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import com.example.newsFeed.users.dto.UserResponseDto;
import com.example.newsFeed.users.dto.UserUpdateRequestDto;
import com.example.newsFeed.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
>>>>>>> a0daa437bbd665795405705d1d964f7df1b7fe88

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
<<<<<<< HEAD
=======

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.findUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping()
    public ResponseEntity<UserResponseDto> updateUserInformation(
            @Valid @RequestBody UserUpdateRequestDto updateDto,
            HttpServletRequest request
    ) {
        //Token에 저장되어 있는 유저 아이디를 가져왔다고 가정하기
        Long userId = 1L;
        UserResponseDto responseDto = userService.updateUserInfo(userId, updateDto);
        return ResponseEntity.ok(responseDto);
    }
>>>>>>> a0daa437bbd665795405705d1d964f7df1b7fe88
}
