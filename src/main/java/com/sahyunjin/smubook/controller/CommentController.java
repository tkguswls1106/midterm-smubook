package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.comment.CommentCreateRequestDto;
import com.sahyunjin.smubook.service.comment.CommentServiceInterface;
import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final FeedServiceInterface feedServiceInterface;
    private final CommentServiceInterface commentServiceInterface;


    @PostMapping("/feeds/{feedId}/comments")  // 댓글 1개 생성
    public Long createComment(@PathVariable Long feedId, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return commentServiceInterface.createComment(feedId, commentCreateRequestDto);
    }

    @GetMapping("/feeds/{feedId}/comments")  // 특정 feed에 해당하는 댓글들 모두 조회 (commentId 기준으로 내림차순 정렬 = 댓글 최신 생성순 정렬)
    public List<Comment> getAllComments(@PathVariable Long feedId) {
        return feedServiceInterface.readAllComments(feedId);
    }
}
