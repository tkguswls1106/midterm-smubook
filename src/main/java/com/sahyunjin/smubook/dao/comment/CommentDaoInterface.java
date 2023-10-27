package com.sahyunjin.smubook.dao.comment;

import com.sahyunjin.smubook.domain.comment.Comment;

import java.util.List;

public interface CommentDaoInterface {

    Long create(Long writeUserId, Long ownerFeedId, String content);

    Comment readById(Long commentId);
    List<Comment> readAllByFeedId(Long feedId);

    void delete(Long commentId);

    boolean existById(Long commentId);
}
