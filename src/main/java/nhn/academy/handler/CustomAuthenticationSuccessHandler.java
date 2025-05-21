package nhn.academy.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhn.academy.service.LoginFailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedisTemplate<String,Object> redisTemplate;

    private final LoginFailService loginFailService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        loginFailService.resetFailCount(request.getParameter("id"));
        log.info("로그인 횟수 초기화완료 -> {}", loginFailService.getFailCount(request.getParameter("id")));

        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        redisTemplate.opsForValue().set(sessionId, authentication.getName());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
