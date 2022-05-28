package com.coldrain.jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// javax.servlet.Filter 인터페이스를 구현하면 필터가 된다.
@Slf4j
public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //log.info("필터3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰을 보냈다고 가정한다. ( 토큰: cos )
        // 필터3은 SecurityConfig 에 걸려있음
        // 아이디와 비밀번호가 정상적으로 입력되어 로그인이 완료되면 토큰을 만들어주고 토큰을 응답 해준다.
        // 그럼 다음 요청부터는 header 의 Authorization 에 value 값으로 토큰을 가지고 요청한다.
        // 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증만 해주면 된다. ( RSA, HS256 )
        if (req.getMethod().equals("POST")) {
            // 헤더에서 Authorization 값을 가지고 온다.
            String authorization = req.getHeader("Authorization");
            // 아무 설정도 없으면 ""로 출력된다.
            // 포스트맨으로 POST 방식으로 해서 Header 에 Authorization 값으로 Hello 라고 보내서 테스트한다.
            log.info("authorization = {}", authorization);

            if (authorization.equals("cos")) { // 토큰이 있음
                // 해당 필터가 끝나면 다음 필터로 넘겨주어야 한다.
                chain.doFilter(req, res);
            } else { // 토큰이 없음
                PrintWriter out = res.getWriter();
                out.println("인증 안 됨!");
            }
        }
    }
}