package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedDeleteRequestDto {

    private String username;

    @Builder
    public FeedDeleteRequestDto(String username) {
        this.username = username;
    }
}
