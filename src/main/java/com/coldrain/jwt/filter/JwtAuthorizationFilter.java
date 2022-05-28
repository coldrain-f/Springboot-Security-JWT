package com.coldrain.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.coldrain.jwt.auth.PrincipalDetails;
import com.coldrain.jwt.model.User;
import com.coldrain.jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티는 다양한 Filter 들을 가지고 있는데
// 그 필터중에 BasicAuthenticationFilter 라는게 있다.
// 이 필터는 권한이나 인증이 필요한 특정 주소를 요청해도 거치게 되어있고
// 권한이나 인증이 필요하지 않은 주소를 요청해도 무조건 거치게 되어있다. ( 토큰이 있는지 없는지 검사 )
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader = {}", jwtHeader);

        // JWT 토큰을 검증해서 정상적인 사용자 인지 확인한다.

        // header 가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인한다.
        // [Bearer ]을 헤더에서 떼어낸다.
        String jwtToken = request.getHeader("Authorization")
                .replace("Bearer ", "");

        // 시크릿키로 jwtToken 을 복호화하고 username 을 가지고 온다.
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(jwtToken)
                .getClaim("username")
                .asString();

        // 서명이 정상적으로 된 사용자
        if (username != null) {
            User user = userRepository.findByUsername(username);

            // 서명이 정상적으로 된 사용자이므로 Authentication 객체를 생성해 준다.
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            // 순서대로 UserDetails, 비밀번호, 권한 정보를 넣어준다.
            // 실제로 로그인하려는 게 아니므로 비밀번호는 null 을 넣어준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여  authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

        // super.doFilterInternal 은 맨 밑에 있어야 한다.
        super.doFilterInternal(request, response, chain);
    }
}
