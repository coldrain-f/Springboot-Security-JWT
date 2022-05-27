package com.coldrain.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration // 스프링 설정 파일
public class CorsConfig {

    @Bean // SecurityConfig 에 등록을 해줘야 한다.
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 서버가 응답을 할 때 JSON 을 자바스크립트에서 처리할 수 있게 할지를 설정한다.
        // 이 옵션이 false 로 되어있으면 자바스크립트로 서버에 요청을 했을 때 응답이 오지 않는다.
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*"); // 모든 IP에 응답을 허용한다.
        corsConfiguration.addAllowedHeader("*"); // 모든 header 에 응답을 허용한다.
        corsConfiguration.addAllowedMethod("*"); // 모든 POST, GET, PUT, DELETE, PATCH 요청을 허용한다.
        source.registerCorsConfiguration("/api/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
