package com.coldrain.jwt.auth;

import com.coldrain.jwt.model.User;
import com.coldrain.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 시큐리티 기본 로그인 주소가 /login 이다.
// 이 서비스는 http://localhost:8080/login POST 요청을 하면 동작한다.
// 시큐리티 설정에서 formLogin().disable() 을 해둬서 현재 프로젝트는 해당 요청이 동작 안 한다.
// 그러므로 강제로 동작하도록 필터를 하나 만들어줘야 한다. -> JwtAuthenticationFilter
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
