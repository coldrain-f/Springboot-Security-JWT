package com.coldrain.jwt.config;

import com.coldrain.jwt.filter.MyFilter1;
import com.coldrain.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // 여기에 등록된 필터들은 스프링 시큐리티 필터들이 모두 끝나고 나서야 실행된다.
    // 스프링 시큐리티 필터들 보다 먼저 실행되게 하고 싶다면 SecurityConfig 에
    // http.addFilterBefore() 을 사용해야 한다.

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); // 모든 요청에 필터를 거치도록 설정
        bean.setOrder(0); // 필터의 우선순위 ( 낮을수록 우선순위가 높고 먼저 실행된다. )
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // MyFilter1 이 먼저 실행되고 MyFilter2 가 실행된다.
        return bean;
    }
}
