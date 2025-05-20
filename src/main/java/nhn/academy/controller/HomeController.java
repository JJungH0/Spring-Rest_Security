package nhn.academy.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView home = new ModelAndView("home"); // home.html 뷰 반환
        home.addObject("loginName", userDetails.getUsername()); // 로그인 사용자 이름을 모델에 담음
        return home;
    }

}