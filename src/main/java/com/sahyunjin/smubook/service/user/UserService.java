package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserDaoInterface userDaoInterface;
    private final FeedDaoInterface feedDaoInterface;


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

    @Override
    public List<User> readAllUsers() {
        return userDaoInterface.readAll();
    }

    @Override
    public void updateFollowUsers(Long userId, UserUpdateFollowsRequestDto userUpdateFollowsRequestDto) {
        User user = userDaoInterface.readById(userId);
        List<User> followUsers = user.getFollowUsers();
        followUsers.add(userUpdateFollowsRequestDto.getUser());
        user.setFollowUsers(followUsers);

        userDaoInterface.update(user);
    }

    @Override
    public void updateFeeds(Long userId, UserUpdateFeedsRequestDto userUpdateFeedsRequestDto) {
        User user = userDaoInterface.readById(userId);
        List<Feed> feeds = user.getFeeds();
        feeds.add(userUpdateFeedsRequestDto.getFeed());
        user.setFeeds(feeds);

        userDaoInterface.update(user);
    }


}
