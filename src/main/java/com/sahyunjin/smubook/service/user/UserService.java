package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.dao.feed.FeedDaoInterface;
import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserDaoInterface userDaoInterface;
    private final FeedDaoInterface feedDaoInterface;


    public LocalDateTime convertModifiedDate(String modifiedDate) {  // 날짜 정렬에 사용할 string형식의 날짜를 localDateTime형식으로 변환하는 메소드이다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"));
        LocalDateTime dateTimeModifiedDate = LocalDateTime.parse(modifiedDate, formatter);
        return dateTimeModifiedDate;
    }


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
            Collections.sort(users, (u1, u2) -> u1.getUsername().compareTo(u2.getUsername()));  // username을 기준으로 사용자들을 오름차순 정렬.
            return users;
        }
        else {
            throw new RuntimeException("ERROR - 리스트에서 사용자 제거 중 에러가 발생했습니다.");
        }
    }

    @Override
    public List<Feed> readMeAndFollowFeeds(Long userId) {

        User user;
        User addSortUser;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
            addSortUser = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }
        List<User> followUsers = user.getFollowUsers();
        followUsers.add(addSortUser);  // users 리스트에 본인과 팔로잉 사용자들 리스트가 포함되게함.

        List<Feed> followFeeds = new ArrayList<Feed>();  // 본인과 팔로잉 사용자들의 모든 글들이 리스트에 저장되게함.
        Iterator<User> iterator = followUsers.iterator();
        while (iterator.hasNext()) {
            List<Feed> feeds = iterator.next().getFeeds();
            followFeeds.addAll(feeds);
        }

        Comparator<Feed> dateComparator = (feed1, feed2) -> convertModifiedDate(feed2.getModifiedDate()).compareTo(convertModifiedDate(feed1.getModifiedDate()));
        Collections.sort(followFeeds, dateComparator);  // 생성 및 수정시각에 따라서 내림차순으로 글을 정렬.

        return followFeeds;
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

        User addUser;
        if (userDaoInterface.existById(userUpdateFollowsRequestDto.getUserId())) {
            addUser = userDaoInterface.readById(userUpdateFollowsRequestDto.getUserId());
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        List<User> followUsers = user.getFollowUsers();
        if (userUpdateFollowsRequestDto.isAdd()) {
            followUsers.add(addUser);
        }
        else {
            Iterator<User> iterator = followUsers.iterator();
            while (iterator.hasNext()) {
                User followUser = iterator.next();
                if (followUser.getId().equals(userUpdateFollowsRequestDto.getUserId())) {
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

        Feed addFeed;
        if (feedDaoInterface.existById(userUpdateFeedsRequestDto.getFeedId())) {
            addFeed = feedDaoInterface.readById(userUpdateFeedsRequestDto.getFeedId());
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        List<Feed> feeds = user.getFeeds();
        if (userUpdateFeedsRequestDto.isAdd()) {
            feeds.add(addFeed);
        }
        else {
            Iterator<Feed> iterator = feeds.iterator();
            while (iterator.hasNext()) {
                Feed feed = iterator.next();
                if (feed.getId().equals(userUpdateFeedsRequestDto.getFeedId())) {
                    iterator.remove();
                }
            }
        }
        user.setFeeds(feeds);

        userDaoInterface.update(user);
    }
}
