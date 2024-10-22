package com.sahyunjin.smubook.domain.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedCreateRequestDto {

    private Long writerUserId;
    private String content;

    @Builder
    public FeedCreateRequestDto(Long writerUserId, String content) {
        this.writerUserId = writerUserId;
        this.content = content;
    }
}
