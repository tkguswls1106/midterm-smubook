package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserLoginRequestDto;
import com.sahyunjin.smubook.domain.user.UserSignupRequestDto;
import com.sahyunjin.smubook.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
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
}
