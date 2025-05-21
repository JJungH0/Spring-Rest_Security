package nhn.academy.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nhn.academy.service.LoginFailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final LoginFailService loginFailService;

    public CustomAuthenticationSuccessHandler(LoginFailService loginFailService) {
        this.loginFailService = loginFailService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        loginFailService.resetFailCount(request.getParameter("id"));
        log.info("로그인 횟수 초기화완료 -> {}", loginFailService.getFailCount(request.getParameter("id")));

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
