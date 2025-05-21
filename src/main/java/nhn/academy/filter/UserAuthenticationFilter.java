package nhn.academy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import nhn.academy.model.AuthUser;
import nhn.academy.model.MemberEntity;
import nhn.academy.service.MemberService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private RedisTemplate<String,Object> redisTemplate;
    private MemberService memberService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String sessionId = null;

        // 쿠키 추출
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if("SESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        // Redis에서 사용자 정보 조회
        if(sessionId != null) {
            Object o = redisTemplate.opsForValue().get(sessionId);
            String memberId = (String) o;

            // 회원 상세 정보 흭득
            MemberEntity memberEntity = memberService.getMember(memberId);
            // AuthUser 객체로 감쌈
            AuthUser authUser = new AuthUser(memberEntity);
            // 인증 객체 생성
            Authentication auth = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
            // 인증 객체 등록
            // 이 후 컨트롤러/서브시에서 @AuthenticationPrincipal으로 사용자 정보를 꺼내쓸 수 있음
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
