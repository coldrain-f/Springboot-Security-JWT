package com.coldrain.jwt.config;

import com.coldrain.jwt.filter.MyFilter1;
import com.coldrain.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 1. CorsConfig 에서 빈으로 등록해주었던 필터를 주입받는다.
    private final CorsFilter corsFilter;

    @Override // JWT 사용시 기본 설정
    protected void configure(HttpSecurity http) throws Exception {
        // 커스텀 필터는 addFilter() 로 걸면 안 되고 addFilterBefore()나 addFilterAfter()로 걸어야 한다.
        // 시큐리티 필터가 실행되기 전에 커스텀 필터가 실행되도록 걸어주어야 한다.
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);

        http.csrf().disable(); // ?

        http.sessionManagement()
                // Session 을 사용하지 않도록 설정한다.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 시큐리티와 다른점 1.
                .and()
                // 2. CorsConfig 에서 만들어준 필터를 등록해준다.
                // 필터가 잘 걸려있는지 확인하려면 자바스크립트로 요청을 해보면 된다.
                .addFilter(corsFilter) // 시큐리티와 다른점 2. ( 모든 요청을 허용하겠음 )
                // JWT 를 사용하므로 폼 로그인을 비활성화 한다. ( <form> 태그로 로그인 하는 것을 비활성화 )
                .formLogin().disable() // 시큐리티와 다른점 3.
                // 기본적인 HTTP 로그인 방식을 사용하지 않도록 비활성화 한다.
                // HTTP headers 의 Authorization 키값에 ID와 비밀번호를 담아서 인증하는 방식 ( http basic )
                // 쿠키와 세션을 사용하지 않는다. 암호화가 되지 않기 때문에 보안에 취약하다 방법은 https 를 사용하는 것
                // Authorization 키값에 Token(JWT) 을 담는 방식은 Bearer 방식이다.
                .httpBasic().disable() // 시큐리티와 다른점 3.
                .authorizeRequests()
                // 경로별 권한 설정
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                // 이외의 모든 경로는 권한 없이 접근할 수 있음
                .anyRequest().permitAll();
    }
}