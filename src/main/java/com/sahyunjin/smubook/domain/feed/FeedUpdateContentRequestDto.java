package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedUpdateContentRequestDto {

    private String username;
    private String content;

    @Builder
    public FeedUpdateContentRequestDto(String username, String content) {
        this.username = username;
        this.content = content;
    }
}
