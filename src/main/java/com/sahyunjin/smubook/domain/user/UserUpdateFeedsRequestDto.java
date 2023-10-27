package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFeedsRequestDto {

    private Long feedId;
    private boolean boolAdd;  // 피드 추가의 경우인지 삭제의 경우인지

    @Builder
    public UserUpdateFeedsRequestDto(Long feedId, boolean boolAdd) {
        this.feedId = feedId;
        this.boolAdd = boolAdd;
    }
}
