package com.sahyunjin.smubook.domain.user;

import com.sahyunjin.smubook.domain.feed.Feed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateFeedsRequestDto {

    private Feed feed;

    @Builder
    public UserUpdateFeedsRequestDto(Feed feed) {
        this.feed = feed;
    }
}
