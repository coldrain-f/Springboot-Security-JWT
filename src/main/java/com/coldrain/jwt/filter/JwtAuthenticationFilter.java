package com.coldrain.jwt.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// POST 방식으로 /login 을 username 과 password 를 가지고 요청하면
// UsernamePasswordAuthenticationFilter 가 동작한다.
// 현재 프로젝트에서는 시큐리티 설정에서 formLogin().disable()을 해둬서 작동하지 않는다.
// 동작을 하게 하려면 JwtAuthenticationFilter 를 등록해 주어야한다. -> SecurityConfig
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 메소드이다.
    // /login 요청을 하면 UsernamePasswordAuthenticationFilter 에 걸린다.
    // attemptAuthentication 메소드 에서 username 과 password 로 데이터베이스에서 확인을 한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("JwtAuthenticationFilter: 로그인 시도중!");

        // 1. username 과 password 를 받는다.

        // 2. 정상인지 로그인 시도를 해본다.
        // authenticationManager 로 로그인 시도를 하고
        // authenticationManager 가 PrincipalDetailsService 가 동작하고
        // 그럼 내부적으로 loadUserByUsername() 메서드가 호출된다.

        // 3. PrincipalDetails 를 세션에 담고
        // 굳이 세션에 담는 이유는? 담지 않으면 권한 관리가 안 된다. 설정파일에 antMatchers() 확인

        // 4. JWT 토큰을 만들어서 응답해주면 된다.
        return super.attemptAuthentication(request, response);
    }
}
