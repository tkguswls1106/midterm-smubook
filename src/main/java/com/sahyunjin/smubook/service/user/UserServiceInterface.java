package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserLoginRequestDto;
import com.sahyunjin.smubook.domain.user.UserSignupRequestDto;

public interface UserServiceInterface {

    Long signUp(UserSignupRequestDto userSignupRequestDto);
    User login(UserLoginRequestDto userLoginRequestDto);
}
