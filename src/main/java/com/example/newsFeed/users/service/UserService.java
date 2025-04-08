package com.example.newsFeed.users.service;

import com.example.newsFeed.config.PasswordEncoder;
import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.dto.LoginRequestDto;
import com.example.newsFeed.users.dto.UserSignUpRequestDto;
import com.example.newsFeed.users.repository.UserRepository;
import com.example.newsFeed.users.dto.UserResponseDto;
import com.example.newsFeed.users.dto.UserUpdateRequestDto;
import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.validation.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpRequestDto signUpDto){
        //이미 존재하는 이메일인지 확인
        if(userRepository.existsByEmail((signUpDto.getEmail()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }
        //새로운 이메일이라면 저장
        //비밀번호 암호화, 날짜 형식 변경 문자열 -> dateTime
        //날짜는 임시 -> 추후 변경
        String encodePassword = PasswordEncoder.encode(signUpDto.getPassword());
        LocalDateTime date = LocalDateTime.parse(signUpDto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        User user = User.toEntity(signUpDto, encodePassword, date);
        userRepository.save(user);
    }

    public UserResponseDto findUserById(Long userId){
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.toDto(user);
    }

    public UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto updateDto){
        User user = userRepository.findByIdOrElseThrow(userId);

        //이름 변경
        //hasText로 null, "", " "를 한번에 검사
        if(StringUtils.hasText(updateDto.getName())){
            user.updateName(updateDto.getName());
        }
        //소개 변경, "" 또는 " " 허용
        if(updateDto.getIntroduction()!=null){
            user.updateIntroduction(updateDto.getIntroduction());
        }
        //비밀번호 변경
        if(StringUtils.hasText(updateDto.getCurrentPassword())&&StringUtils.hasText(updateDto.getUpdatePassword())){
            //변경할 비밀번호 형식 검사
            if(!PasswordValidator.isValid(updateDto.getUpdatePassword())){
                throw new IllegalArgumentException("대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야합니다.");
            }

            //현재 비밀번호 확인
            if(!user.isPasswordEqual(updateDto.getCurrentPassword(), passwordEncoder)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
            }

            user.updatePassword(updateDto.getUpdatePassword(), passwordEncoder);
        }
        //저장
        userRepository.save(user);
        return UserResponseDto.toDto(user);
    }

    public Long login(LoginRequestDto requestDto) {

      User user = userRepository.findUserByEmailOrElseThrow(requestDto.getEmail());

     if(!PasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
         throw new CustomException(Errors.INVALID_PASSWORD);
     }

     return user.getId();
    }
}
