package com.example.newsFeed.users.service;

import com.example.newsFeed.boards.repository.BoardRepository;
import com.example.newsFeed.global.exception.CustomException;
import com.example.newsFeed.global.exception.Errors;
import com.example.newsFeed.jwt.dto.LoginRequestDto;
import com.example.newsFeed.relation.repository.RelationshipRepository;
import com.example.newsFeed.users.dto.*;
import com.example.newsFeed.users.repository.UserRepository;
import com.example.newsFeed.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final RelationshipRepository relationshipRepository;

    public void signUp(UserSignUpRequestDto signUpDto) {
        //이미 존재하는 이메일인지 확인
        if (userRepository.existsByEmail((signUpDto.getEmail()))) {
            throw new CustomException(Errors.EMAIL_DUPLICATION);
        }
        //새로운 이메일이라면 저장
        User user = User.toEntity(signUpDto);
        userRepository.save(user);
    }

    public Page<UserListResponseDto> getUserPage(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByEnableTrue(pageable);
        return users.map(UserListResponseDto::toDto);
    }


    //id로 User 찾아서 UserResponseDto로 반환
    public UserResponseDto findUserById(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        if (!user.isEnable()){
            throw new CustomException(Errors.USER_NOT_FOUND, "This user has already been deleted");
        }
        return UserResponseDto.toDto(user, boardRepository.countByUserId(userId), relationshipRepository.countAcceptedFriends(user));
    }

    //유저 아이디로 유저 Entity 찾아서 반환
    public User getUserById(Long userId) {
        return userRepository.findByIdOrElseThrow(userId);
    }

    //유저 정보 업데이트
    public UserResponseDto updateUserInfo(Long userId, UserUpdateRequestDto updateDto) {
        User user = userRepository.findByIdOrElseThrow(userId);
        //이름 변경
        user.updateName(updateDto.getName());
        //소개 변경
        user.updateIntroduction(updateDto.getIntroduction());
        //저장
        userRepository.save(user);
        return UserResponseDto.toDto(user, boardRepository.countByUserId(userId), relationshipRepository.countAcceptedFriends(user));
    }

    public void updateUserPassword(Long id, UserPasswordRequestDto passwordDto) {
        User user = userRepository.findByIdOrElseThrow(id);
        //현재 비밀번호가 일치하는지 확인 후 비밀번호 변경
        if (!user.checkPasswordEqual(passwordDto.getCurrentPassword())) {
            throw new CustomException(Errors.INVALID_PASSWORD);
        }
        user.updatePassword(passwordDto.getUpdatePassword());
        userRepository.save(user);
    }

    public void resignUser(Long id, UserDeleteRequestDto deleteDto) {
        User user = userRepository.findByIdOrElseThrow(id);

        if (!user.isEnable()){
            throw new CustomException(Errors.USER_NOT_FOUND, "This user has already been deleted");
        }
        if (!user.checkPasswordEqual(deleteDto.getPassword())) {
            throw new CustomException(Errors.INVALID_PASSWORD);
        }
        user.updateEnableFalse();
        userRepository.save(user);
    }

    public Long login(LoginRequestDto requestDto) {

        User user = userRepository.findUserByEmailOrElseThrow(requestDto.getEmail());
        if (!user.isEnable()){
            throw new CustomException(Errors.USER_NOT_FOUND, "This user has already been deleted");
        }

        if (!user.checkPasswordEqual(requestDto.getPassword())) {
            throw new CustomException(Errors.INVALID_PASSWORD);
        }

        return user.getId();
    }
}