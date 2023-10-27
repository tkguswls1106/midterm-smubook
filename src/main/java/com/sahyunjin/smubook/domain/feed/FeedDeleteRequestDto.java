package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedDeleteRequestDto {

    private String username;

    @Builder
    public FeedDeleteRequestDto(String username) {
        this.username = username;
    }
}
