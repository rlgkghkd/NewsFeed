package com.example.newsFeed.Users.service;

import com.example.newsFeed.Users.Repository.UserRepository;
import com.example.newsFeed.Users.dto.UserResponseDto;
import com.example.newsFeed.Users.dto.UserUpdateRequestDto;
import com.example.newsFeed.Users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto findUserById(Long userId){
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.toDto(user);
    }

    public UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto updateDto){
        User user = userRepository.findByIdOrElseThrow(userId);

        //이름 변경
        if(updateDto.getName()!=null){
            user.updateName(updateDto.getName());
        }
        //소개 변경
        if(updateDto.getIntroduction()!=null){
            user.updateIntroduction(updateDto.getIntroduction());
        }
        //비밀번호 변경
        if(updateDto.getCurrentPassword()!=null && updateDto.getUpdatePassword()!=null){
            user.updatePassword(updateDto.getCurrentPassword(), updateDto.getUpdatePassword());
        }
        //저장
        userRepository.save(user);
        return UserResponseDto.toDto(user);
    }
}
