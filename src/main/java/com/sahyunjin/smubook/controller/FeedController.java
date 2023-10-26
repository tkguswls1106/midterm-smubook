package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.feed.*;
import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import com.sahyunjin.smubook.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/feeds")
public class FeedController {

    private final UserServiceInterface userServiceInterface;
    private final FeedServiceInterface feedServiceInterface;
    // 차후 CommentService에 Feed객체의 commentList 요소 추가 관련코드도 추가하기!!! create와 delete 모두 케스케이드 제거 및 추가시키기!!!


    @PostMapping("/feeds")
    public Long createFeed(@RequestBody FeedCreateRequestDto feedCreateRequestDto) {
        return feedServiceInterface.createFeed(feedCreateRequestDto);
    }

    @GetMapping("/users/{userId}/feeds")
    public List<Feed> getAllFeeds(@PathVariable Long userId) {
        return userServiceInterface.readMeAndFollowFeeds(userId);
    }

    @PutMapping("/feeds/{feedId}")
    public void updateContent(@PathVariable Long feedId, @RequestBody FeedUpdateContentRequestDto feedUpdateContentRequestDto) {
        feedServiceInterface.updateContent(feedId, feedUpdateContentRequestDto);
//        return feedServiceInterface.readFeed(feedId);
    }

    @PutMapping("/like-feed/{feedId}")
    public void updateLike(@PathVariable Long feedId, @RequestBody FeedUpdateLikeRequestDto feedUpdateLikeRequestDto) {
        feedServiceInterface.updateLike(feedId, feedUpdateLikeRequestDto);
//        return feedServiceInterface.readFeed(feedId);
    }

    @DeleteMapping("/feeds/{feedId}")
    public void deleteFeed(@PathVariable Long feedId, @RequestBody FeedDeleteRequestDto feedDeleteRequestDto) {
        feedServiceInterface.deleteFeed(feedId, feedDeleteRequestDto);
    }
}
