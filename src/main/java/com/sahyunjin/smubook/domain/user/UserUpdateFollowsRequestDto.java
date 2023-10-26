package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFollowsRequestDto {

    private User user;
    private boolean isAdd;  // 팔로우 추가의 경우인지 삭제의 경우인지

    @Builder
    public UserUpdateFollowsRequestDto(User user, boolean isAdd) {
        this.user = user;
        this.isAdd = isAdd;
    }
}
