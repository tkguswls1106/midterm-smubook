package com.sahyunjin.smubook.dao.comment;

import com.sahyunjin.smubook.domain.comment.Comment;

public interface CommentDaoInterface {

    Long create(Long writeUserId, Long ownerFeedId, String content);

    Comment readById(Long commentId);

    void delete(Long commentId);

    boolean existById(Long commentId);
}
