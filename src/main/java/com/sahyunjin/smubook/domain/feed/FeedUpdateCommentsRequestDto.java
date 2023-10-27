package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedUpdateCommentsRequestDto {

    private Long commentId;
    private boolean boolAdd;  // 댓글 추가의 경우인지 삭제의 경우인지

    @Builder
    public FeedUpdateCommentsRequestDto(Long commentId, boolean boolAdd) {
        this.commentId = commentId;
        this.boolAdd = boolAdd;
    }
}
