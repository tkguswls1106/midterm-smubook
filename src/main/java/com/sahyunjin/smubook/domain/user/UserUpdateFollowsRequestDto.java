package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFollowsRequestDto {

    private Long userId;
    private boolean boolAdd;  // 팔로우 추가의 경우인지 삭제의 경우인지

    @Builder
    public UserUpdateFollowsRequestDto(Long userId, boolean boolAdd) {
        this.userId = userId;
        this.boolAdd = boolAdd;
    }
}
