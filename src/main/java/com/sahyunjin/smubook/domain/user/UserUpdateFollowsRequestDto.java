package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFollowsRequestDto {

    private User user;

    @Builder
    public UserUpdateFollowsRequestDto(User user) {
        this.user = user;
    }
}
