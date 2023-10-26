package com.sahyunjin.smubook.service.comment;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.comment.CommentCreateRequestDto;

public interface CommentServiceInterface {

    Long createComment(Long feedId, CommentCreateRequestDto commentCreateRequestDto);

    Comment readComment(Long commentId);
}
