package com.sahyunjin.smubook.dao.comment;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.User;

public interface CommentDaoInterface {

    Long create(User writeUser, Feed ownerFeed, String content);

    Comment readById(Long commentId);

    void delete(Long commentId);

    boolean existById(Long commentId);
}
