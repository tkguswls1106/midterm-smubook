package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.comment.Comment;
import com.sahyunjin.smubook.domain.comment.CommentCreateRequestDto;
import com.sahyunjin.smubook.domain.user.User;
import com.sahyunjin.smubook.service.comment.CommentServiceInterface;
import com.sahyunjin.smubook.service.feed.FeedServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final FeedServiceInterface feedServiceInterface;
    private final CommentServiceInterface commentServiceInterface;


    @PostMapping("/feeds/{feedId}/comments")  // 댓글 1개 생성
    public String createComment(@PathVariable Long feedId, @ModelAttribute CommentCreateRequestDto commentCreateRequestDto, HttpSession session) {

        User loginUser;
        try {  // 로그인 체크
            loginUser = loginCheckSession(session);
        }
        catch (RuntimeException e) {  // 로그인이 안되어있을시, 로그인창으로 강제 리다이렉트
            return "redirect:/login";
        }

        commentServiceInterface.createComment(feedId, commentCreateRequestDto);

        return "redirect:/users/" + loginUser.getId() + "/feeds";
    }

    @GetMapping("/feeds/{feedId}/comments")  // 특정 feed에 해당하는 댓글들 모두 조회 (commentId 기준으로 내림차순 정렬 = 댓글 최신 생성순 정렬)
    public List<Comment> getAllComments(@PathVariable Long feedId) {
        return feedServiceInterface.readAllComments(feedId);
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
