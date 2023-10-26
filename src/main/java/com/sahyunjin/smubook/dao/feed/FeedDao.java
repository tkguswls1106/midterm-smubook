package com.sahyunjin.smubook.dao.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahyunjin.smubook.domain.feed.Feed;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Repository
public class FeedDao implements FeedDaoInterface {

    private Map<Long, Feed> feedMap;
    private ObjectMapper objectMapper;
    private final String dataFilePath = "feed_data.json";

    public FeedDao() {
        this.objectMapper = new ObjectMapper();
        this.loadFeedMap();
        if (feedMap == null) {
            this.feedMap = new LinkedHashMap<>();
        }
    }

    // 사용자 데이터를 파일에 저장
    private void saveFeedMap() {
        try {
            objectMapper.writeValue(new File(dataFilePath), feedMap);
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 저장 중 에러가 발생했습니다.");
        }
    }

    // 파일에서 사용자 데이터 로드
    private void loadFeedMap() {
        try {
            File file = new File(dataFilePath);

            if (!file.exists()) {
                file.createNewFile();  // 파일이 없을 경우 생성
                objectMapper.writeValue(file, new LinkedHashMap<>());
            }
            else if (file.exists() && (file.length() == 0))
                objectMapper.writeValue(file, new LinkedHashMap<>());

            feedMap = objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, Long.class, Feed.class));
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 로드 중 에러가 발생했습니다.");
        }
    }

    // 새로운 id 생성
    private Long generateNewId() {
        long maxId = feedMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return maxId + 1;
    }


    @Override
    public Long create(Long writeUserId, String content) {
        Long newFeedId = generateNewId();
        String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko")));
        Feed feed = new Feed(newFeedId, content, modifiedDate, writeUserId, new ArrayList<Long>(), 0, new ArrayList<Long>());

        feedMap.put(feed.getId(), feed);
        this.saveFeedMap();
        return feed.getId();
    }

    @Override
    public Feed readById(Long feedId) {  // 에러가 탑재되어있지않으므로, 차후 service로직에서 예외처리해야함.
        return feedMap.get(feedId);
    }

    @Override
    public void update(Feed feed) {
        if (feedMap.containsKey(feed.getId())) {
            feedMap.put(feed.getId(), feed);
            this.saveFeedMap();
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }
    }

    @Override
    public void delete(Long feedId) {  // 에러가 탑재되어있으므로, 차후 service로직에서는 예외처리 안해도됨.
        if (feedMap.containsKey(feedId)) {
            feedMap.remove(feedId);
            this.saveFeedMap();
        }
        else {
            throw new RuntimeException("ERROR - 해당 글은 존재하지 않습니다.");
        }
    }

    @Override
    public boolean existById(Long feedId) {
        return feedMap.containsKey(feedId);
    }
}
