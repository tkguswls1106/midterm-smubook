package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedUpdateLikeRequestDto {

    private String username;
    private boolean boolLike;  // 좋아요 추가의 경우인지 삭제의 경우인지

    @Builder
    public FeedUpdateLikeRequestDto(String username, boolean boolLike) {
        this.username = username;
        this.boolLike = boolLike;
    }
}
