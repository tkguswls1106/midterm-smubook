package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.comment.CommentCreateRequestDto;
import com.sahyunjin.smubook.service.comment.CommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceInterface commentServiceInterface;


    @PostMapping("/feeds/{feedId}/comments")
    public Long createComment(@PathVariable Long feedId, @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return commentServiceInterface.createComment(feedId, commentCreateRequestDto);
    }
}
