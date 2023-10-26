package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserLoginRequestDto;
import com.sahyunjin.smubook.domain.user.UserSignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserDaoInterface userDaoInterface;


    @Override
    public Long signUp(UserSignupRequestDto userSignupRequestDto) {
        if(!userDaoInterface.existByUsername(userSignupRequestDto.getUsername())) {
            userDaoInterface.create(userSignupRequestDto.getUsername(), userSignupRequestDto.getPassword());
            return userDaoInterface.readByUsername(userSignupRequestDto.getUsername()).getId();
        }
        else {
            throw new RuntimeException("ERROR - 이미 존재하는 username의 회원가입입니다.");
        }
    }

    @Override
    public User login(UserLoginRequestDto userLoginRequestDto) {
        if(userDaoInterface.existByAccount(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())) {
            return userDaoInterface.readByUsername(userLoginRequestDto.getUsername());
        }
        else {
            throw new RuntimeException("ERROR - 로그인정보가 일치하지않습니다.");
        }
    }
}
