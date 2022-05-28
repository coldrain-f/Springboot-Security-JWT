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
// 이 서비스는 http://localhost:8080/login 요청을 하면 동작한다.
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService.loadUserByUsername 호출!");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
