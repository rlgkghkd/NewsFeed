package com.example.newsFeed.users.service;

import com.example.newsFeed.users.dto.UserPasswordRequestDto;
import com.example.newsFeed.users.dto.UserSignUpRequestDto;
import com.example.newsFeed.users.repository.UserRepository;
import com.example.newsFeed.users.dto.UserResponseDto;
import com.example.newsFeed.users.dto.UserUpdateRequestDto;
import com.example.newsFeed.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(UserSignUpRequestDto signUpDto){
        //이미 존재하는 이메일인지 확인
        if(userRepository.existsByEmail((signUpDto.getEmail()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }
        //새로운 이메일이라면 저장
        //날짜 형식 변경 문자열 -> dateTime
        LocalDateTime date = LocalDateTime.parse(signUpDto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        //비밀번호 암호화 후 생성
        User user = User.toEntity(signUpDto, date);
        userRepository.save(user);
    }

    public UserResponseDto findUserById(Long userId){
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.toDto(user);
    }

    public UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto updateDto){
        User user = userRepository.findByIdOrElseThrow(userId);
        //이름 변경
        user.updateName(updateDto.getName());
        //소개 변경
        user.updateIntroduction(updateDto.getIntroduction());
        //저장
        userRepository.save(user);
        return UserResponseDto.toDto(user);
    }

    public void updateUserPassword(Long id, UserPasswordRequestDto passwordDto){
        User user = userRepository.findByIdOrElseThrow(id);
        //현재 비밀번호가 일치하는지 확인 후 비밀번호 변경
        if(!user.isPasswordEqual(passwordDto.getCurrentPassword())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(passwordDto.getUpdatePassword());
    }
}
