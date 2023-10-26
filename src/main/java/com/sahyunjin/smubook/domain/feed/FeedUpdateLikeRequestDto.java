package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedUpdateLikeRequestDto {

    private String username;
    private boolean isLike;  // 좋아요 추가의 경우인지 삭제의 경우인지

    @Builder
    public FeedUpdateLikeRequestDto(String username, boolean isLike) {
        this.username = username;
        this.isLike = isLike;
    }
}
