package com.sahyunjin.smubook.service.user;

import com.sahyunjin.smubook.dao.feed.FeedDaoInterface;
import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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


    @Transactional
    @Override
    public Long signUp(UserSignupRequestDto userSignupRequestDto) {

        if (!userDaoInterface.existByUsername(userSignupRequestDto.getUsername())) {
            return userDaoInterface.create(userSignupRequestDto.getUsername(), userSignupRequestDto.getPassword());
        }
        else {
            throw new RuntimeException("ERROR - 이미 존재하는 username의 회원가입입니다.");
        }
    }

    @Transactional
    @Override
    public User login(UserLoginRequestDto userLoginRequestDto) {

        if (userDaoInterface.existByAccount(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())) {
            return userDaoInterface.readByUsername(userLoginRequestDto.getUsername());
        }
        else {
            throw new RuntimeException("ERROR - 로그인정보가 일치하지않습니다.");
        }
    }

    @Transactional
    @Override
    public User readUser(Long userId) {

        if (userDaoInterface.existById(userId)) {
            return userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }
    }

    @Transactional
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

    @Transactional
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

        List<Long> followUserIds = user.getFollowUserIds();
        List<User> followUsers = new ArrayList<User>();
        for (Long followUserId : followUserIds) {
            User followUser = userDaoInterface.readById(followUserId);
            if (followUser != null) {
                followUsers.add(followUser);
            }
            else {
                throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
            }
        }
        followUsers.add(addSortUser);  // followUsers 리스트에 본인과 팔로잉 사용자들 리스트가 포함되게함.

        List<Feed> followFeeds = new ArrayList<Feed>();  // 본인과 팔로잉 사용자들의 모든 글들이 리스트에 저장되게함.
        Iterator<User> iterator = followUsers.iterator();
        while (iterator.hasNext()) {
            List<Long> followFeedIds = iterator.next().getFeedIds();
            for (Long followFeedId : followFeedIds) {
                Feed followFeed = feedDaoInterface.readById(followFeedId);
                if (followFeed != null) {
                    followFeeds.add(followFeed);
                }
            }
        }

        Set<Long> uniqueIds = new HashSet<>();  // feedId가 중복되는 feed객체를 제거하기위해서.
        List<Feed> uniqueFollowFeeds = new ArrayList<>();
        for (Feed feed : followFeeds) {
            if (!uniqueIds.contains(feed.getId())) {
                uniqueIds.add(feed.getId());
                uniqueFollowFeeds.add(feed);
            }
        }

        Comparator<Feed> dateComparator = (feed1, feed2) -> convertModifiedDate(feed2.getModifiedDate()).compareTo(convertModifiedDate(feed1.getModifiedDate()));
        Collections.sort(uniqueFollowFeeds, dateComparator);  // 생성 및 수정시각에 따라서 내림차순으로 글을 정렬.

        return uniqueFollowFeeds;
    }

    @Transactional
    @Override
    public void updateFollowUsers(Long userId, UserUpdateFollowsRequestDto userUpdateFollowsRequestDto) {

        User user;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        Long addUserId;
        if (userDaoInterface.existById(userUpdateFollowsRequestDto.getUserId())) {
            addUserId = userUpdateFollowsRequestDto.getUserId();
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        List<Long> followUserIds = user.getFollowUserIds();
        if (userUpdateFollowsRequestDto.isBoolAdd()) {
            if (!followUserIds.contains(addUserId))
                followUserIds.add(addUserId);
        }
        else {
            Iterator<Long> iterator = followUserIds.iterator();
            while (iterator.hasNext()) {
                Long followUserId = iterator.next();
                if (followUserId.equals(userUpdateFollowsRequestDto.getUserId())) {
                    iterator.remove();
                }
            }
        }
        user.setFollowUserIds(followUserIds);

        userDaoInterface.update(user);
    }

    @Transactional
    @Override
    public void updateFeeds(Long userId, UserUpdateFeedsRequestDto userUpdateFeedsRequestDto) {

        User user;
        if (userDaoInterface.existById(userId)) {
            user = userDaoInterface.readById(userId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        Long addFeedId = userUpdateFeedsRequestDto.getFeedId();
        // deleteFeed 메소드 내에서 feed삭제후 updateFeed 메소드 호출을 하게되면 feed가 존재하지않는걸로 간주되어 에러처리되기때문에, 밑부분은 주석처리하였음.
//        Long addFeedId;
//        if (feedDaoInterface.existById(userUpdateFeedsRequestDto.getFeedId())) {
//            addFeedId = userUpdateFeedsRequestDto.getFeedId();
//        }
//        else {
//            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
//        }

        List<Long> feedIds = user.getFeedIds();
        if (userUpdateFeedsRequestDto.isBoolAdd()) {
            if (!feedIds.contains(addFeedId))
                feedIds.add(addFeedId);
        }
        else {
            Iterator<Long> iterator = feedIds.iterator();
            while (iterator.hasNext()) {
                Long feedId = iterator.next();
                if (feedId.equals(userUpdateFeedsRequestDto.getFeedId())) {
                    iterator.remove();
                }
            }
        }
        user.setFeedIds(feedIds);

        userDaoInterface.update(user);
    }
}
