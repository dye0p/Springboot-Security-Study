package com.springbootproject.springbootsecuritytest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //시큐리티 활성화 (스프링 시큐리티 필터체인에 등록)
@EnableMethodSecurity(securedEnabled = true) //메서드 수준의 시큐리티 보안 활성화
public class SecurityConfig {

    /*
     * @Bean : 해당 메서드의 리턴되는 오브젝트는 IoC로 등록함
     * */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((authorizeRequest) ->
                        authorizeRequest
                                .requestMatchers("/user/**").authenticated() //인증만 되면 접근 가능
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll() //위 3개의 url을 제외하고 모두 접근 가능
                );

        //폼 기반 로그인 url 설정(인증이 필요한 url로 접근시 로그인 페이지로 리다이렉트)
        http
                .formLogin((form) ->
                        form.loginPage("/loginForm"))

                .formLogin((form) ->
                        form.loginProcessingUrl("/login")//login주소가 호출이되면 시큐리티 낚아채서 대신 로그인을 진행함
                                .defaultSuccessUrl("/")) //로그인이 성공되면 '/'로 이동

                .oauth2Login((oauth2) ->
                        oauth2.loginPage("/loginForm"))
        ;
        return http.build();
    }
}
