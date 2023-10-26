package com.sahyunjin.smubook.dao.feed;

import com.sahyunjin.smubook.domain.feed.Feed;

public interface FeedDaoInterface {

    Long create(Long writeUserId, String content);

    Feed readById(Long feedId);

    void update(Feed feed);

    void delete(Long feedId);

    boolean existById(Long feedId);
}
