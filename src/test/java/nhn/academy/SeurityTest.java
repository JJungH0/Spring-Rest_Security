package nhn.academy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest // 스프링 부트를 전체 구동하여 통합 테스트 수행
@AutoConfigureMockMvc // MockMvc 자동설정
class SeurityTest {

    // GET, POST, PUT 등 HTTP 요청 시뮬레이션 -> 검증 가능
    @Autowired
    private MockMvc mockMvc;

    @Test
    // 해당 가짜 로그인 세션을 생성
    // 내부적으로 {"USER"} -> ROLE_USER 라는 권한이 생성됨
    @WithMockUser(username = "user", roles = {"USER"})
    void notfound() throws Exception {
        // 실제 /public 이라는 URL 매핑이 없으로 404 Not Found 발생
        mockMvc.perform(MockMvcRequestBuilders.get("/public"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    // 이번엔 roles 대신 authorities -> {"ROLE_ADMIN"}사용
    @WithMockUser(username = "user", authorities = {"ROLE_ADMIN"})
    void admin2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MEMBER"})
    void admin_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                // 권한부족 -> 403 에러 발생
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // 권한이 존재하지 않은 사용자 접속 여부
    // public-project 경로 -> permitAll() 설정되어 있기에
    // 비인증 사용자도 접근 가능한 공개 URL
    @Test
    @WithMockUser(username = "user", authorities = {})
    void noRoleRedirectPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public-project"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // 익명사용자 접속시 허용 여부
    @Test
    void anonymousAccessToPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public-project"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // 익명 사용자가 /admin 페이지로 접근시 .anyRequest().authenticated() 정책에 의해
    // 로그인페이지로 이동하는지 검증
    @Test
    void anonymousAccessToAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }
}
