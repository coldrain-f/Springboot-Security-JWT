package com.coldrain.jwt.controller;

import com.coldrain.jwt.auth.PrincipalDetails;
import com.coldrain.jwt.model.Role;
import com.coldrain.jwt.model.User;
import com.coldrain.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        return "<h1>Home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    // 회원가입
    // 포스트맨으로 테스트 해보면 된다.
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return "회원가입 완료!";
    }

    // user, manager, admin 권한 접근 가능
    @GetMapping("/api/v1/user") // 세션 가져오는 방법..
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "user";
    }

    // manager, admin 권한 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin 권한 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}