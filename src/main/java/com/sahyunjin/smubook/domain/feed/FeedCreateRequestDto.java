package com.sahyunjin.smubook.domain.feed;

import com.sahyunjin.smubook.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
