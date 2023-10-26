package com.sahyunjin.smubook.domain.user;

import com.sahyunjin.smubook.domain.feed.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFeedsRequestDto {

    private Long feedId;
    private boolean isAdd;  // 피드 추가의 경우인지 삭제의 경우인지

    @Builder
    public UserUpdateFeedsRequestDto(Long feedId, boolean isAdd) {
        this.feedId = feedId;
        this.isAdd = isAdd;
    }
}
