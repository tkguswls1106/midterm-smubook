package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.feed.*;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import com.sahyunjin.smubook.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final UserServiceInterface userServiceInterface;
    private final FeedServiceInterface feedServiceInterface;


    @PostMapping("/feeds")  // feed 1개 생성
    public Long createFeed(@RequestBody FeedCreateRequestDto feedCreateRequestDto) {
        return feedServiceInterface.createFeed(feedCreateRequestDto);
    }

    @GetMapping("/users/{userId}/feeds")  // 본인과 팔로잉사용자들의 feed들 모두 조회 (최신 수정순 정렬)
    public List<Feed> getAllFeeds(@PathVariable Long userId) {
        return userServiceInterface.readMeAndFollowFeeds(userId);
    }

    @PutMapping("/feeds/{feedId}")  // feed의 내용 수정 (로그인사용자가 feed 작성자인 경우에만 수정 가능)
    public void updateContent(@PathVariable Long feedId, @RequestBody FeedUpdateContentRequestDto feedUpdateContentRequestDto) {
        feedServiceInterface.updateContent(feedId, feedUpdateContentRequestDto);
    }

    @PutMapping("/like-feed/{feedId}")  // feed의 Like 기능 (개인당 최대 1번까지 가능. 이미 Like누른사용자의 경우에는 unlike만 가능.)
    public void updateLike(@PathVariable Long feedId, @RequestBody FeedUpdateLikeRequestDto feedUpdateLikeRequestDto) {
        feedServiceInterface.updateLike(feedId, feedUpdateLikeRequestDto);
    }

    @DeleteMapping("/feeds/{feedId}")  // feed 삭제 (삭제시 'feed,Like,댓글' 전부 일괄삭제. 로그인사용자가 feed 작성자인 경우에만 삭제 가능.)
    public void deleteFeed(@PathVariable Long feedId, @RequestBody FeedDeleteRequestDto feedDeleteRequestDto) {
        feedServiceInterface.deleteFeed(feedId, feedDeleteRequestDto);
    }


    // ----- 기타 사용 메소드들 ----- //

    public User loginCheckSession(HttpSession session) {  // 로그인 체크용 메소드
        User user = (User) session.getAttribute("user");
        if (user != null) {  // 로그인되어있는 경우
            return user;
        } else {
            throw new RuntimeException("ERROR - 로그인이 되어있지않습니다.");
        }
    }


    @GetMapping("/main_feed")
    public String getMainFeedPage(Model model, HttpSession session){

        User user;
        try {
            user = loginCheckSession(session);
        }
        catch (RuntimeException e) {
            return "redirect:/login";
        }

        model.addAttribute("userId", user.getId());
        return "main_feed";
    }
}
