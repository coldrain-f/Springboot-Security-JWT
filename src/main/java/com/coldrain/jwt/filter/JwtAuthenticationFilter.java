package com.coldrain.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.coldrain.jwt.auth.PrincipalDetails;
import com.coldrain.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// POST 방식으로 /login 을 username 과 password 를 가지고 요청하면
// UsernamePasswordAuthenticationFilter 가 동작한다.
// 현재 프로젝트에서는 시큐리티 설정에서 formLogin().disable()을 해둬서 작동하지 않는다.
// 동작을 하게 하려면 JwtAuthenticationFilter 를 등록해 주어야한다. -> SecurityConfig
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 메소드이다.
    // /login 요청을 하면 UsernamePasswordAuthenticationFilter 에 걸린다.
    // attemptAuthentication 메소드 에서 username 과 password 로 데이터베이스에서 확인을 한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("JwtAuthenticationFilter: 로그인 시도중!");

        // 1. /login 요청시 보낸 username 과 password 를 받는다.
        try {
            // JSON 으로 요청했다고 가정한다.
            // ObjectMapper 가 JSON 데이터를 파싱해준다.
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            // Token 을 생성한다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 2. 생성한 Token 을 가지고 로그인 시도를 해본다.
            // authenticationManager.authenticate() 로 로그인 시도를 하면
            // 내부적으로 PrincipalDetailsService 의 loadUserByUsername() 메서드가 호출된다.
            // authentication 에는 로그인 정보가 담긴다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 로그인 정보 확인
            // principalDetails 를 잘 가지고 온다면 로그인을 성공 했다는 뜻이다.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("principalDetails.username = {}", principalDetails.getUsername());

            // authentication 객체는 반환되는 시점에 session 영역에 저장된다.
            // 권한 관리를 시큐리티가 대신 하도록 맡기기 위해서 세션 영역에 저장하는 것이다.
            // JWT 토큰을 사용하면 세션을 만들 이유는 없다. 권한 처리 때문.
            return authentication;

        } catch (BadCredentialsException e) {
            // 시큐리티 Exception 정리:
            // https://theheydaze.tistory.com/307
            // AuthenticationProvider 를 커스텀 하여 아이디가 없는 경우엔 UsernameNotFoundException
            // 비밀번호가 틀린 경우에는 BadCredentialsException 을 발생시킨 후
            // AuthenticationFailureHandler 에서 Exception 을 처리해 주면 된다.
            log.error("로그인 실패... 아이디 혹은 비밀번호 불일치");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication() 에서 인증에 성공했다면 successfulAuthentication() 이 호출된다.
    // 이곳에서 JWT 토큰을 생성하여 사용자에게 JWT 토큰을 응답해 주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication: 인증이 완료되었음");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        // java JWT 라이브러리 사용
        String jwtToken = JWT.create()
                // 토큰 이름
                .withSubject(principalDetails.getUsername())
                // 토큰 만료 시간 ( 현재 시간 + 지정 시간 )
                // 밀리 세컨드는 1000이 1초이다. (60000 * 10) = 10분
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                // 토큰에 담고싶은 데이터
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        // 응답 헤더에 담긴다.
        response.addHeader("Authorization", "Bearer " + jwtToken);
        // 서버가 JWT 토큰이 유효한지 판단하기 위한 Filter 를 만들어야 한다.
        // -> JwtAuthorizationFilter 확인
    }
}