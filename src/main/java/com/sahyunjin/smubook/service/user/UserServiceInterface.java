package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.domain.user.*;

import java.util.List;

public interface UserServiceInterface {

    Long signUp(UserSignupRequestDto userSignupRequestDto);
    User login(UserLoginRequestDto userLoginRequestDto);
    List<User> readAllUsers();
    void updateFollowUsers(Long userId, UserUpdateFollowsRequestDto userUpdateFollowsRequestDto);
    void updateFeeds(Long userId, UserUpdateFeedsRequestDto userUpdateFeedsRequestDto);
}
