package com.sahyunjin.smubook.controller;

import com.sahyunjin.smubook.domain.user.*;
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
public class UserController {

    private final UserServiceInterface userServiceInterface;


    @GetMapping("/")  // 기본페이지 접속시 로그인 페이지로 리이렉트시킴
    public String homeRedirect(Model model) {
        return "redirect:/login";  // login 페이지로 리다이렉트
    }

    @GetMapping("/signup")  // 회원가입 페이지로 이동
    public String signUpForm(Model model) {
        return "signup";
    }
    @GetMapping("/signup_error")  // 회원가입에러 페이지로 이동
    public String signupError() {
        return "signup_error";
    }
    @PostMapping("/signup")  // 회원가입
    public String signUp(@ModelAttribute UserSignupRequestDto userSignupRequestDto) {
        try {
            userServiceInterface.signUp(userSignupRequestDto);
            return "redirect:/login";  // login 페이지로 리다이렉트
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return "redirect:/signup_error";  // signup_error 페이지로 리다이렉트
        }

    }

    @GetMapping("/login")  // 로그인 페이지로 이동
    public String loginForm() {
        return "login";
    }
    @GetMapping("/login_error")  // 로그인에러 페이지로 이동
    public String loginError() {
        return "login_error";
    }
    @PostMapping("/login")  // 로그인 처리
    public String login(@ModelAttribute UserLoginRequestDto userLoginRequestDto, HttpSession session) {
        try {
            User loginUser = userServiceInterface.login(userLoginRequestDto);

            session.setAttribute("user", loginUser);

            return "redirect:/main_feed";  // main_feed 페이지로 리다이렉트
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return "redirect:/login_error";  // login_error 페이지로 리다이렉트
        }
    }

    @GetMapping("/users/{userId}")  // 본인을 제외한 팔로잉사용자들을 모두 조회 (username 기준으로 오름차순 정렬)
    public String getAllUsers(@PathVariable Long userId, Model model, HttpSession session) {

        loginCheckSession(session);  // 로그인 체크

        List<Long> followUserIds = userServiceInterface.readUser(userId).getFollowUserIds();
        List<User> followUsers = new ArrayList<User>();
        for (Long followUserId : followUserIds) {
            User followUser = userServiceInterface.readUser(followUserId);
            if (followUser != null) {
                followUsers.add(followUser);
            }
            else {
                throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
            }
        }

        List<User> users = userServiceInterface.readOtherUsers(userId);
        List<Boolean> boolFollowList = containsInA(followUsers, users);

        List<UserResponseDto> userResponseDtos = new ArrayList<UserResponseDto>();
        for (int i=0; i<users.size(); i++) {
            UserResponseDto userResponseDto = new UserResponseDto(users.get(i).getId(), users.get(i).getUsername(), boolFollowList.get(i).booleanValue());
            userResponseDtos.add(userResponseDto);
        }

        model.addAttribute("userResponseDtos", userResponseDtos);
        model.addAttribute("userId", userId);

        return "friends";
    }

    @PutMapping("/users/{myId}")  // 사용자 follow 기능 (이미 follow누른사용자의 경우에는 unlike 가능.)
    public String updateFollowUsers(@PathVariable Long myId, @ModelAttribute UserUpdateFollowsRequestDto userUpdateFollowsRequestDto, HttpSession session) {
        loginCheckSession(session);  // 로그인 체크

        userServiceInterface.updateFollowUsers(myId, userUpdateFollowsRequestDto);
        return "redirect:/users/" + myId;
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

    public static <T> List<Boolean> containsInA(List<T> A, List<T> B) {
        List<Boolean> boolResults = new ArrayList<>();

        for (T itemB : B) {
            boolean contains = A.contains(itemB);
            boolResults.add(contains);
        }

        return boolResults;
    }
}
