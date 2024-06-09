package com.springbootproject.springbootsecuritytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //시큐리티 활성화 (스프링 시큐리티 필터체인에 등록)
public class SecurityConfig {

    /*
    * @Bean : 해당 메서드의 리턴되는 오브젝트는 IoC로 등록함
    * */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((authorizeRequest) ->
                        authorizeRequest
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll() //위 3개의 url을 제외하고 모두 접근 가능
                );

        //폼 기반 로그인 url 설정(인증이 필요한 url로 접근시 로그인 페이지로 리다이렉트)
        http
                .formLogin((form) ->
                        form.loginPage("/loginForm"));
        return http.build();
    }
}
