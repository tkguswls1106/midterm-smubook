package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
