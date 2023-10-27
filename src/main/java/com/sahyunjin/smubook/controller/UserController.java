package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserLoginRequestDto;
import com.sahyunjin.smubook.domain.user.UserSignupRequestDto;
import com.sahyunjin.smubook.domain.user.UserUpdateFollowsRequestDto;
import com.sahyunjin.smubook.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceInterface userServiceInterface;


    @PostMapping("/signup")  // 회원가입
    public Long signUp(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        return userServiceInterface.signUp(userSignupRequestDto);
    }

    @PostMapping("/login")  // 로그인
    public User login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return userServiceInterface.login(userLoginRequestDto);
    }

    @GetMapping("/users/{userId}")  // 본인을 제외한 팔로잉사용자들을 모두 조회 (username 기준으로 오름차순 정렬)
    public List<User> getAllUsers(@PathVariable Long userId) {
        return userServiceInterface.readOtherUsers(userId);
    }

    @PutMapping("/users/{userId}")  // 사용자 follow 기능 (이미 follow누른사용자의 경우에는 unlike 가능.)
    public void updateFollowUsers(@PathVariable Long userId, @RequestBody UserUpdateFollowsRequestDto userUpdateFollowsRequestDto) {
        userServiceInterface.updateFollowUsers(userId, userUpdateFollowsRequestDto);
//        return userServiceInterface.readUser(userId);
    }
}
