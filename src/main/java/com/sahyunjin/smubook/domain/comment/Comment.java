package com.sahyunjin.smubook.domain.comment;

import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private String content;

    private User writerUser;
    private Feed ownerFeed;
}
