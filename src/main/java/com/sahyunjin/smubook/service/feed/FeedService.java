package com.sahyunjin.smubook.service.feed;

import com.sahyunjin.smubook.dao.comment.CommentDaoInterface;
import com.sahyunjin.smubook.dao.feed.FeedDaoInterface;
import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.*;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserUpdateFeedsRequestDto;
import com.sahyunjin.smubook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FeedService implements FeedServiceInterface {

    private final UserDaoInterface userDaoInterface;
    private final FeedDaoInterface feedDaoInterface;
    private final CommentDaoInterface commentDaoInterface;
    private final UserService userService;

    @Override
    public Long createFeed(FeedCreateRequestDto feedCreateRequestDto) {

        User writeUser;
        if (userDaoInterface.existById(feedCreateRequestDto.getWriterUserId())) {
            writeUser = userDaoInterface.readById(feedCreateRequestDto.getWriterUserId());
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        Long newFeedId = feedDaoInterface.create(writeUser, feedCreateRequestDto.getContent());  // feed에 생성 후에
        userService.updateFeeds(feedCreateRequestDto.getWriterUserId(), new UserUpdateFeedsRequestDto(newFeedId, true));  // user에도 feed 속성 업데이트.

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
                feed.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))));
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
            if (!likeUsers.contains(userDaoInterface.readByUsername(feedUpdateLikeRequestDto.getUsername()))) {  // 아직 좋아요를 누른사람이 아니라면
                likeUsers.add(userDaoInterface.readByUsername(feedUpdateLikeRequestDto.getUsername()));
                feed.setLikeCount(feed.getLikeCount()+1);
            }
            else {
                throw new RuntimeException("ERROR - 사용자는 이미 해당 글에 좋아요를 누른 상태입니다.");
            }
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

        Comment addComment;
        if (commentDaoInterface.existById(feedUpdateCommentsRequestDto.getCommentId())) {
            addComment = commentDaoInterface.readById(feedUpdateCommentsRequestDto.getCommentId());
        }
        else {
            throw new RuntimeException("ERROR - 해당 댓글은 존재하지 않습니다.");
        }

        List<Comment> comments = feed.getComments();
        if (feedUpdateCommentsRequestDto.isAdd()) {
            comments.add(addComment);
        }
        else {
            Iterator<Comment> iterator = comments.iterator();
            while (iterator.hasNext()) {
                Comment comment = iterator.next();
                if (comment.getId().equals(feedUpdateCommentsRequestDto.getCommentId())) {
                    iterator.remove();
                }
            }
        }
        feed.setComments(comments);

        feedDaoInterface.update(feed);
    }

    @Override
    public void deleteFeed(Long feedId, FeedDeleteRequestDto feedDeleteRequestDto) {  // 글 삭제시, 글과 좋아요와 댓글들이 모두 함께 삭제된다.

        List<Comment> deleteComments;
        Feed deleteFeed;
        if (feedDaoInterface.existById(feedId)) {
            deleteComments = feedDaoInterface.readById(feedId).getComments();
            deleteFeed = feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        if (userDaoInterface.existByUsername(feedDeleteRequestDto.getUsername())) {
            User loginUser = userDaoInterface.readByUsername(feedDeleteRequestDto.getUsername());

            if (loginUser.getId() == deleteFeed.getWriterUser().getId()) {  // 해당 글의 작성자가 로그인계정과 일치한다면
                Iterator<Comment> iterator = deleteComments.iterator();
                while (iterator.hasNext()) {
                    Long deleteCommentId = iterator.next().getId();
                    commentDaoInterface.delete(deleteCommentId);  // 해당 글 내의 댓글 먼저 삭제.
                }
                feedDaoInterface.delete(feedId);  // 그 후에 글 삭제.
                userService.updateFeeds(loginUser.getId(), new UserUpdateFeedsRequestDto(feedId, false));  // 그 후에 User객체의 글 리스트에서 해당 글 객체를 제거.
            }
            else {
                throw new RuntimeException("ERROR - 해당 글을 수정할 권한이 없는 사용자입니다.");
            }
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }
    }

}
