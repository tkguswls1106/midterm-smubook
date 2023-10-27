package com.sahyunjin.smubook.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
