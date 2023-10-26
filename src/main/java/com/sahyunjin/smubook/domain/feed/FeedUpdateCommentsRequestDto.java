package com.sahyunjin.smubook.domain.feed;

import com.sahyunjin.smubook.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedUpdateCommentsRequestDto {

    private Long commentId;
    private boolean isAdd;  // 댓글 추가의 경우인지 삭제의 경우인지

    @Builder
    public FeedUpdateCommentsRequestDto(Long commentId, boolean isAdd) {
        this.commentId = commentId;
        this.isAdd = isAdd;
    }
}
