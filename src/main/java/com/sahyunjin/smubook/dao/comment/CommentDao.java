package com.sahyunjin.smubook.dao.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.Feed;
import com.sahyunjin.smubook.domain.user.User;
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
public class CommentDao implements CommentDaoInterface {

    private Map<Long, Comment> commentMap;
    private ObjectMapper objectMapper;
    private final String dataFilePath = "comment_data.json";

    public CommentDao() {
        this.objectMapper = new ObjectMapper();
        this.loadCommentMap();
        if (commentMap == null) {
            this.commentMap = new LinkedHashMap<>();
        }
    }

    // 사용자 데이터를 파일에 저장
    private void saveCommentMap() {
        try {
            objectMapper.writeValue(new File(dataFilePath), commentMap);
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 저장 중 에러가 발생했습니다.");
        }
    }

    // 파일에서 사용자 데이터 로드
    private void loadCommentMap() {
        try {
            File file = new File(dataFilePath);
            if (file.exists()) {
                commentMap = objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, Long.class, Comment.class));
            }
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 로드 중 에러가 발생했습니다.");
        }
    }

    // 새로운 id 생성
    private Long generateNewId() {
        long maxId = commentMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return maxId + 1;
    }


    @Override
    public Long create(User writeUser, String content) {
        Long newCommentId = generateNewId();
        Comment comment = new Comment(newCommentId, content, writeUser);

        commentMap.put(comment.getId(), comment);
        this.saveCommentMap();
        return comment.getId();
    }

    @Override
    public Comment readById(Long commentId) {  // 에러가 탑재되어있지않으므로, 차후 service로직에서 예외처리해야함.
        return commentMap.get(commentId);
    }

    @Override
    public boolean existById(Long commentId) {
        return commentMap.containsKey(commentId);
    }
}