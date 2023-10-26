package com.sahyunjin.smubook.service.feed;

import com.sahyunjin.smubook.dao.feed.FeedDaoInterface;
import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.*;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserUpdateFeedsRequestDto;
import com.sahyunjin.smubook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedServiceInterface {

    private final UserDaoInterface userDaoInterface;
    private final FeedDaoInterface feedDaoInterface;
    private final UserService userService;

    @Override
    public Long createFeed(FeedCreateRequestDto feedCreateRequestDto) {

        Long newFeedId = feedDaoInterface.create(feedCreateRequestDto.getContent(), feedCreateRequestDto.getWriterUser());
        Feed newFeed = feedDaoInterface.readById(newFeedId);
        userService.updateFeeds(feedCreateRequestDto.getWriterUser().getId(), new UserUpdateFeedsRequestDto(newFeed, true));

        return newFeedId;
    }

    @Override
    public Feed readFeed(Long feedId) {

        if (feedDaoInterface.existById(feedId)) {
            return feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }
    }

    @Override
    public void updateContent(Long feedId, FeedUpdateContentRequestDto feedUpdateContentRequestDto) {

        Feed feed;
        if (feedDaoInterface.existById(feedId)) {
            feed = feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        if (userDaoInterface.existByUsername(feedUpdateContentRequestDto.getUsername())) {
            if (userDaoInterface.readByUsername(feedUpdateContentRequestDto.getUsername()).getId() == feed.getWriterUser().getId()) {  // 해당 글의 작성자가 로그인계정과 일치한다면
                feed.setContent(feedUpdateContentRequestDto.getContent());
            }
            else {
                throw new RuntimeException("ERROR - 해당 글을 수정할 권한이 없는 사용자입니다.");
            }
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        feedDaoInterface.update(feed);
    }

    @Override
    public void updateLike(Long feedId, FeedUpdateLikeRequestDto feedUpdateLikeRequestDto) {

        Feed feed;
        if (feedDaoInterface.existById(feedId)) {
            feed = feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        List<User> likeUsers = feed.getLikeUsers();
        if (feedUpdateLikeRequestDto.isLike()) {
            likeUsers.add(userDaoInterface.readByUsername(feedUpdateLikeRequestDto.getUsername()));
            feed.setLikeCount(feed.getLikeCount()+1);
        }
        else {
            Iterator<User> iterator = likeUsers.iterator();
            while (iterator.hasNext()) {
                User likeUser = iterator.next();
                if (likeUser.equals(userDaoInterface.readByUsername(feedUpdateLikeRequestDto.getUsername()))) {
                    iterator.remove();
                    if (feed.getLikeCount()-1 >= 0)
                        feed.setLikeCount(feed.getLikeCount()-1);
                }
            }
        }
        feed.setLikeUsers(likeUsers);

        feedDaoInterface.update(feed);
    }

    @Override
    public void updateComments(Long feedId, FeedUpdateCommentsRequestDto feedUpdateCommentsRequestDto) {

        Feed feed;
        if (feedDaoInterface.existById(feedId)) {
            feed = feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        List<Comment> comments = feed.getComments();
        if (feedUpdateCommentsRequestDto.isAdd()) {
            comments.add(feedUpdateCommentsRequestDto.getComment());
        }
        else {
            Iterator<Comment> iterator = comments.iterator();
            while (iterator.hasNext()) {
                Comment comment = iterator.next();
                if (comment.equals(feedUpdateCommentsRequestDto.getComment())) {
                    iterator.remove();
                }
            }
        }
        feed.setComments(comments);

        feedDaoInterface.update(feed);
    }

    @Override
    public void deleteFeed(Long feedId) {

        feedDaoInterface.delete(feedId);
    }

}
