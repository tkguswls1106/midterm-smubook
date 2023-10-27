package com.sahyunjin.smubook.dao.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahyunjin.smubook.domain.comment.Comment;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

            if (!file.exists()) {
                file.createNewFile();  // 파일이 없을 경우 생성
                objectMapper.writeValue(file, new LinkedHashMap<>());
            }
            else if (file.exists() && (file.length() == 0))
                objectMapper.writeValue(file, new LinkedHashMap<>());

            commentMap = objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, Long.class, Comment.class));
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
    public Long create(Long writeUserId, Long ownerFeedId, String content) {
        Long newCommentId = generateNewId();
        Comment comment = new Comment(newCommentId, content, writeUserId, ownerFeedId);

        commentMap.put(comment.getId(), comment);
        this.saveCommentMap();
        return comment.getId();
    }

    @Override
    public Comment readById(Long commentId) {  // 에러가 탑재되어있지않으므로, 차후 service로직에서 예외처리해야함.
        return commentMap.get(commentId);
    }

    @Override
    public List<Comment> readAllByFeedId(Long feedId) {
        List<Comment> comments = new ArrayList<Comment>();

        for (Comment comment : commentMap.values()) {
            if (comment.getOwnerFeedId().equals(feedId)) {
                comments.add(comment);
            }
        }

        return comments;
    }

    @Override
    public void delete(Long commentId) {  // 에러가 탑재되어있으므로, 차후 service로직에서는 예외처리 안해도됨.
        if (commentMap.containsKey(commentId)) {
            commentMap.remove(commentId);
            this.saveCommentMap();
        }
        else {
            throw new RuntimeException("ERROR - 해당 댓글은 존재하지 않습니다.");
        }
    }

    @Override
    public boolean existById(Long commentId) {
        return commentMap.containsKey(commentId);
    }
}
