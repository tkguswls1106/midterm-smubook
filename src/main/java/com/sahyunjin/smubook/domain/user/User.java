package com.sahyunjin.smubook.domain.user;

import com.sahyunjin.smubook.domain.feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;

//    private List<User> followUsers;
//    private List<Feed> feeds;
}
