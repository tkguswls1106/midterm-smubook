package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserDaoInterface userDaoInterface;


    @Override
    public Long signUp(UserSignupRequestDto userSignupRequestDto) {

        if (!userDaoInterface.existByUsername(userSignupRequestDto.getUsername())) {
            return userDaoInterface.create(userSignupRequestDto.getUsername(), userSignupRequestDto.getPassword());
        }
        else {
            throw new RuntimeException("ERROR - 이미 존재하는 username의 회원가입입니다.");
        }
    }

    @Override
    public User login(UserLoginRequestDto userLoginRequestDto) {

        if (userDaoInterface.existByAccount(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())) {
            return userDaoInterface.readByUsername(userLoginRequestDto.getUsername());
        }
        else {
            throw new RuntimeException("ERROR - 로그인정보가 일치하지않습니다.");
        }
    }

    @Override
    public User readUser(Long userId) {

        if (userDaoInterface.existById(userId)) {
            return userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }
    }

    @Override
    public List<User> readOtherUsers(Long userId) {

        User user;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        List<User> users = userDaoInterface.readAll();
        if (users.remove(user)) {
            return users;
        }
        else {
            throw new RuntimeException("ERROR - 리스트에서 사용자 제거 중 에러가 발생했습니다.");
        }
    }

    @Override
    public void updateFollowUsers(Long userId, UserUpdateFollowsRequestDto userUpdateFollowsRequestDto) {

        User user;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        List<User> followUsers = user.getFollowUsers();
        if (userUpdateFollowsRequestDto.isAdd()) {
            followUsers.add(userUpdateFollowsRequestDto.getUser());
        }
        else {
            Iterator<User> iterator = followUsers.iterator();
            while (iterator.hasNext()) {
                User followUser = iterator.next();
                if (followUser.equals(userUpdateFollowsRequestDto.getUser())) {
                    iterator.remove();
                }
            }
        }
        user.setFollowUsers(followUsers);

        userDaoInterface.update(user);
    }

    @Override
    public void updateFeeds(Long userId, UserUpdateFeedsRequestDto userUpdateFeedsRequestDto) {

        User user;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        List<Feed> feeds = user.getFeeds();
        if (userUpdateFeedsRequestDto.isAdd()) {
            feeds.add(userUpdateFeedsRequestDto.getFeed());
        }
        else {
            Iterator<Feed> iterator = feeds.iterator();
            while (iterator.hasNext()) {
                Feed feed = iterator.next();
                if (feed.equals(userUpdateFeedsRequestDto.getFeed())) {
                    iterator.remove();
                }
            }
        }
        user.setFeeds(feeds);

        userDaoInterface.update(user);
    }

}
