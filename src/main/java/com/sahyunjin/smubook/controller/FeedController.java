package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.feed.*;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.service.comment.CommentServiceInterface;
import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import com.sahyunjin.smubook.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final UserServiceInterface userServiceInterface;
    private final FeedServiceInterface feedServiceInterface;
    private final CommentServiceInterface commentServiceInterface;


    @PostMapping("/feeds")  // feed 1개 생성
    public String createFeed(@ModelAttribute FeedCreateRequestDto feedCreateRequestDto, HttpSession session) {

        User loginUser;
        try {  // 로그인 체크
            loginUser = loginCheckSession(session);
        }
        catch (RuntimeException e) {  // 로그인이 안되어있을시, 로그인창으로 강제 리다이렉트
            return "redirect:/login";
        }

        feedServiceInterface.createFeed(feedCreateRequestDto);

        return "redirect:/users/" + loginUser.getId() + "/feeds";
    }

    @GetMapping("/users/{userId}/feeds")  // 본인과 팔로잉사용자들의 feed들 모두 조회 (최신 수정순 정렬)
    public String getAllFeeds(@PathVariable Long userId, Model model, HttpSession session){

        User loginUser;
        try {  // 로그인 체크
            loginUser = loginCheckSession(session);
        }
        catch (RuntimeException e) {  // 로그인이 안되어있을시, 로그인창으로 강제 리다이렉트
            return "redirect:/login";
        }

        List<FeedResponseDto> feedResponseDtos = new ArrayList<FeedResponseDto>();

        List<Feed> feeds = userServiceInterface.readMeAndFollowFeeds(userId);

        for (Feed feed : feeds) {
            FeedResponseDto feedResponseDto = new FeedResponseDto();

            feedResponseDto.setFeed(feed);
            feedResponseDto.setUsername(loginUser.getUsername());

            List<Long> likeUserIds = feed.getLikeUserIds();
            if (likeUserIds.contains(userId)) {
                feedResponseDto.setBoolLike(true);
            }
            else {
                feedResponseDto.setBoolLike(false);
            }

            List<Comment> comments = feedServiceInterface.readAllComments(feed.getId());
            feedResponseDto.setComments(comments);

            feedResponseDtos.add(feedResponseDto);
        }

        model.addAttribute("feedResponseDtos", feedResponseDtos);
        model.addAttribute("userId", userId);

        return "main_feed";
    }

    @PutMapping("/feeds/{feedId}")  // feed의 내용 수정 (로그인사용자가 feed 작성자인 경우에만 수정 가능)
    public void updateContent(@PathVariable Long feedId, @RequestBody FeedUpdateContentRequestDto feedUpdateContentRequestDto) {
        feedServiceInterface.updateContent(feedId, feedUpdateContentRequestDto);
    }

    @PutMapping("/like-feed/{feedId}")  // feed의 Like 기능 (개인당 최대 1번까지 가능. 이미 Like누른사용자의 경우에는 unlike만 가능.)
    public String updateLike(@PathVariable Long feedId, @ModelAttribute FeedUpdateLikeRequestDto feedUpdateLikeRequestDto, HttpSession session) {

        User loginUser;
        try {  // 로그인 체크
            loginUser = loginCheckSession(session);
        }
        catch (RuntimeException e) {  // 로그인이 안되어있을시, 로그인창으로 강제 리다이렉트
            return "redirect:/login";
        }

        feedServiceInterface.updateLike(feedId, feedUpdateLikeRequestDto);
        return "redirect:/users/" + loginUser.getId() + "/feeds";
    }

    @DeleteMapping("/feeds/{feedId}")  // feed 삭제 (삭제시 'feed,Like,댓글' 전부 일괄삭제.)
    public String deleteFeed(@PathVariable Long feedId, @ModelAttribute FeedDeleteRequestDto feedDeleteRequestDto, HttpSession session) {

        User loginUser;
        try {  // 로그인 체크
            loginUser = loginCheckSession(session);
        }
        catch (RuntimeException e) {  // 로그인이 안되어있을시, 로그인창으로 강제 리다이렉트
            return "redirect:/login";
        }

        feedServiceInterface.deleteFeed(feedId, feedDeleteRequestDto);

        return "redirect:/users/" + loginUser.getId() + "/feeds";
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
}
