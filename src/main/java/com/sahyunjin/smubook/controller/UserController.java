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


    @PostMapping("/signup")
    public Long signUp(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        return userServiceInterface.signUp(userSignupRequestDto);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return userServiceInterface.login(userLoginRequestDto);
    }

    @GetMapping("/users/{userId}")
    public List<User> getAllUsers(@PathVariable Long userId) {
        return userServiceInterface.readOtherUsers(userId);
    }

    @PutMapping("/users/{userId}")
    public void updateFollowUsers(@PathVariable Long userId, @RequestBody UserUpdateFollowsRequestDto userUpdateFollowsRequestDto) {
        userServiceInterface.updateFollowUsers(userId, userUpdateFollowsRequestDto);
//        return userServiceInterface.readUser(userId);
    }

}
