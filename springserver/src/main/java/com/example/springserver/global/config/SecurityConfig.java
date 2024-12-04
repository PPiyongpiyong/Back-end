package com.example.springserver.global.config;

import com.example.springserver.api.security.auth.JwtAuthenticationFilter;
import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.global.exception.CustomerAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 비밀번호를 해싱(DB에 비밀번호 그대로 저장하면 안 됨)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        setAccessTokenFilter(httpSecurity);
        setPermissions(httpSecurity);

        httpSecurity.exceptionHandling()
                .accessDeniedHandler(new CustomerAccessDeniedHandler());

        return httpSecurity.build();
    }

    // JwtAuthenticationFilter 추가(API 접근 인증 설정)
    private void setAccessTokenFilter(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(memberRepository, tokenProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 설정(모든 origin 허용)
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration conf = new CorsConfiguration();

        conf.addAllowedOrigin("*"); // 모든 Origin 허용
        conf.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        conf.addAllowedHeader("*"); // 모든 헤더 허용
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf); // 모든 경로에 대하여 설정한 CORS 정책들을 적용

        return source;
    }


    // 인가 설정(경로별 접근 권한 설정)
    private void setPermissions(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // 로그인과 회원가입에 대하여는 누구든 접근 가능
                .anyRequest().authenticated() // 그 외의 모든 요청에 대하여는 권한이 필요
        );

    }
}
