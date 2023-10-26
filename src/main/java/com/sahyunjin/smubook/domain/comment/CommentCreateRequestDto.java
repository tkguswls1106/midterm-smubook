package com.sahyunjin.smubook.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

    private Long writerUserId;
    private String content;

    @Builder
    public CommentCreateRequestDto(Long writerUserId, String content) {
        this.writerUserId = writerUserId;
        this.content = content;
    }
}
