package com.sahyunjin.smubook.domain.feed;

import com.sahyunjin.smubook.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FeedResponseDto {

    private Feed feed;
    private String username;
    private boolean boolLike;
    private List<Comment> comments;

    @Builder
    public FeedResponseDto(Feed feed, String username, boolean boolLike, List<Comment> comments) {
        this.feed = feed;
        this.username = username;
        this.boolLike = boolLike;
        this.comments = comments;
    }
}
