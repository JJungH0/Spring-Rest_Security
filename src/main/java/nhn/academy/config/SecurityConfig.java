package nhn.academy.config;

import nhn.academy.filter.UserAuthenticationFilter;
import nhn.academy.handler.CustomAuthenticationFailureHandler;
import nhn.academy.handler.CustomAuthenticationSuccessHandler;
import nhn.academy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;


//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails member = User.withDefaultPasswordEncoder()
//                .username("member")
//                .password("member")
//                .roles("MEMBER")
//                .build();
//        return new InMemoryUserDetailsManager(admin,member);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                                                   CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                                                   RedisTemplate<String, Object> redisTemplate,       // **추가!**
                                                   MemberService memberService) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) ->
                formLogin.loginPage("/auth/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/auth/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)

        );

        http
                .exceptionHandling()
                .accessDeniedPage("/403");

        http.authorizeHttpRequests(authorizeRequests ->
                // "/admin/** -> URL 요청은 ADMIN 권한만 접근이 가능.
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        // 권한이 ADMIN || MEMBER 접근가능
                        .requestMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                        // 해당 URL에는 모두 접근허용
                        .requestMatchers("/public-project/**").permitAll()

                        // 해당 URL에는 모두 접근허용
                        .requestMatchers("/members").permitAll()

                        // 단일 멤버 조회 허용
                        .requestMatchers("/members/**").permitAll()

                        // 실습추가
                        .requestMatchers("/auth/login").permitAll()

                        // 회원가입 기능접속
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        // 그 외 요청은 로그인(인증) 되어야만 접근 가능
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // csrf diable
        http.csrf(AbstractHttpConfigurer::disable);
        // UsernamePasswordAuthenticationFilter가 활성화
//        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_MEMBER"); //// ROLE_ADMIN은 ROLE_MEMBER보다 상위
    }


}
