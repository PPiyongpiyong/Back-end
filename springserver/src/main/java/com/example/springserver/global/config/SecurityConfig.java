package com.example.springserver.global.config;

import com.example.springserver.api.security.auth.JwtAuthenticationFilter;
import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.global.exception.CustomerExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    // 비밀번호를 해싱
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(
                                corsConfigurationSource()
                        )) //CORS 정책 작성 및 예외
                .csrf(AbstractHttpConfigurer::disable) // CSRF 예외 코드 작성
                .formLogin(AbstractHttpConfigurer::disable);

        //setLoginFilter(httpSecurity);
        setAccessTokenFilter(httpSecurity);
        //setTokenRefreshFilter(httpSecurity);
        setPermissions(httpSecurity);

        return httpSecurity.exceptionHandling(
                eh -> eh.accessDeniedHandler((AccessDeniedHandler) new CustomerExceptionHandler())).build();
    }

    // JwtAuthenticationFilter 추가(API 접근 인증 설정)
    private void setAccessTokenFilter(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 설정(모든 origin 허용)
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration conf = new CorsConfiguration();

        conf.setAllowedOriginPatterns(List.of("*")); // 모든 origin에 대한 접근 허용
        conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        conf.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // 접근 가능한 header 정보값
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf); // 모든 경로에 대하여 설정한 CORS 정책들을 적용

        return source;
    }


    // 로그인 인증 설정
    private void setLoginFilter(HttpSecurity httpSecurity) {

    }

    //
    private void setTokenRefreshFilter(HttpSecurity httpSecurity) {

    }

    // 인가 설정
    private void setPermissions(HttpSecurity httpSecurity) {

    }
}
