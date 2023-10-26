package com.sahyunjin.smubook.service.comment;

import com.sahyunjin.smubook.dao.comment.CommentDaoInterface;
import com.sahyunjin.smubook.dao.feed.FeedDaoInterface;
import com.sahyunjin.smubook.dao.user.UserDaoInterface;
import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.comment.CommentCreateRequestDto;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.feed.FeedUpdateCommentsRequestDto;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.domain.user.UserUpdateFeedsRequestDto;
import com.sahyunjin.smubook.service.feed.FeedService;
import com.sahyunjin.smubook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceInterface {

    private final UserDaoInterface userDaoInterface;
    private final FeedDaoInterface feedDaoInterface;
    private final CommentDaoInterface commentDaoInterface;
    private final UserService userService;
    private final FeedService feedService;
    // 차후 CommentService에 Feed객체의 commentList 요소 추가 관련코드도 추가하기!!! create와 delete 모두 케스케이드 제거 및 추가시키기!!!


    @Override
    public Long createComment(Long feedId, CommentCreateRequestDto commentCreateRequestDto) {

        User writeUser;
        if (userDaoInterface.existById(commentCreateRequestDto.getWriterUserId())) {
            writeUser = userDaoInterface.readById(commentCreateRequestDto.getWriterUserId());
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }

        Feed ownerFeed;
        if (feedDaoInterface.existById(feedId)) {
            ownerFeed = feedDaoInterface.readById(feedId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }

        Long newCommentId = commentDaoInterface.create(writeUser, ownerFeed, commentCreateRequestDto.getContent());  // comment에 생성 후에
        feedService.updateComments(feedId, new FeedUpdateCommentsRequestDto(newCommentId, true));  // feed에 comment 속성 업데이트 후에
        userService.updateFeeds(commentCreateRequestDto.getWriterUserId(), new UserUpdateFeedsRequestDto(feedId, true));  // user에도 feed 속성 업데이트.

        return newCommentId;
    }

    @Override
    public Comment readComment(Long commentId) {

        if (commentDaoInterface.existById(commentId)) {
            return commentDaoInterface.readById(commentId);
        }
        else {
            throw new RuntimeException("ERROR - 해당 댓글은 존재하지 않습니다.");
        }
    }

}
