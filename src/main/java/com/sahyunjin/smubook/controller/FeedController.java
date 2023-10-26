package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/feeds")
public class FeedController {

    private final FeedServiceInterface serviceInterface;
    // 차후 CommentService에 Feed객체의 commentList 요소 추가 관련코드도 추가하기 !!!


    @PostMapping
}
