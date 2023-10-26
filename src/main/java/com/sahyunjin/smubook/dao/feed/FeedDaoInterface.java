package com.sahyunjin.smubook.dao.feed;

import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.User;

public interface FeedDaoInterface {

    Long create(User writeUser, String content);

    Feed readById(Long feedId);

    void update(Feed feed);

    void delete(Long feedId);

    boolean existById(Long feedId);
}
