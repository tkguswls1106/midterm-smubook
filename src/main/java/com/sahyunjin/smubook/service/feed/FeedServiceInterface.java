package com.sahyunjin.smubook.service.feed;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.*;

import java.util.List;

public interface FeedServiceInterface {

    Long createFeed(FeedCreateRequestDto feedCreateRequestDto);

    Feed readFeed(Long feedId);
    List<Comment> readAllComments(Long feedId);

    void updateContent(Long feedId, FeedUpdateContentRequestDto feedUpdateContentRequestDto);
    void updateLike(Long feedId, FeedUpdateLikeRequestDto feedUpdateLikeRequestDto);
    void updateComments(Long feedId, FeedUpdateCommentsRequestDto feedUpdateCommentsRequestDto);

    void deleteFeed(Long feedId, FeedDeleteRequestDto feedDeleteRequestDto);
}
