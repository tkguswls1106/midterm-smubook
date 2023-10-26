package com.sahyunjin.smubook.domain;

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
}
