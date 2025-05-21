package nhn.academy.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nhn.academy.service.LoginFailService;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final LoginFailService loginFailService;

    public CustomAuthenticationFailureHandler(LoginFailService loginFailService) {
        this.loginFailService = loginFailService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

//        String id = request.getParameter("id");
//        Integer loginFailCount = (Integer) request.getSession().getAttribute("loginFailCount");
//        if (loginFailCount == null) {
//            loginFailCount = 0;
//        }
//        loginFailCount++;
//        request.getSession().setAttribute("loginFailCount", loginFailCount);
//        if(loginFailCount >= 5) {
//            log.error("로그인 실패 횟수 5회 초과 -> 사용자 ID = {}",request.getParameter("id"));
//        }

//        Map<String, Integer> failCount = (Map<String, Integer>) request.getSession().getAttribute("failMap");
//
//        if (failCount == null) {
//            failCount = new HashMap<>();
//        }
//        failCount.put(request.getParameter("id"), failCount.getOrDefault(request.getParameter("id"), 0) + 1);
//        request.getSession().setAttribute("failMap", failCount);

        int count = loginFailService.incrementLoginFailCount(request.getParameter("id"));
        if(count >= 5) {
            log.error("로그인 실패 횟수 5회 초과 -> 사용자 ID = {}",request.getParameter("id"));
            loginFailService.setFailCountExpire(request.getParameter("id"),60L); // 1분
            // + Lock 구현 해야함 -> 그냥 로그인됨
        }

        response.sendRedirect("/auth/login?error=true");
    }
}
