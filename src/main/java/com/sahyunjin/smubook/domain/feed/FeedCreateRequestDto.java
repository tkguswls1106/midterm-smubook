package com.sahyunjin.smubook.domain.feed;

import com.sahyunjin.smubook.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedCreateRequestDto {

    private String content;
    private User writerUser;

    @Builder
    public FeedCreateRequestDto(String content, User writerUser) {
        this.content = content;
        this.writerUser = writerUser;
    }
}
