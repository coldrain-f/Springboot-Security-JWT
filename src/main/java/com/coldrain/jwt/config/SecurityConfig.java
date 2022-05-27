package com.coldrain.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 1. CorsConfig 에서 빈으로 등록해주었던 필터를 주입받는다.
    private final CorsFilter corsFilter;

    @Override // JWT 사용시 기본 설정
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // ?

        // Session 을 사용하지 않도록 설정한다.
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 시큐리티와 다른점 1.
                .and()
                // 2. CorsConfig 에서 만들어준 필터를 등록해준다.
                // 필터가 잘 걸려있는지 확인하려면 자바스크립트로 요청을 해보면 된다.
                .addFilter(corsFilter) // 시큐리티와 다른점 2. ( 모든 요청을 허용하겠음 )
                // JWT 를 사용하므로 폼 로그인을 비활성화 한다. ( <form> 태그로 로그인 하는 것을 비활성화 )
                .formLogin().disable() // 시큐리티와 다른점 3.
                // 기본적인 HTTP 로그인 방식을 사용하지 않도록 비활성화 한다.
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
