package nhn.academy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhn.academy.model.MemberEntity;
import nhn.academy.model.MemberLoginRequest;
import nhn.academy.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public ModelAndView processLogin(MemberLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response)  {
        MemberEntity memberResponse = memberService.login(loginRequest);
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("loginName", memberResponse.getName());

        System.out.println("입력 패스워드 -> " + loginRequest.getPassword());
        System.out.println("저장된 암호화 -> " + memberResponse.getPassword());
        System.out.println("매치 결과 -> " + passwordEncoder.matches(loginRequest.getPassword(), memberResponse.getPassword()));
        return mav;
    }


}
