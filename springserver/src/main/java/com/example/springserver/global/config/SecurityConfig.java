package com.example.springserver.global.config;

import com.example.springserver.global.exception.CustomAuthenticationEntryPoint;
import com.example.springserver.global.auth.JwtAuthenticationFilter;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.exception.CustomerAccessDeniedHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.springserver.global.config.CustomLoginFailureHandler;
import java.util.List;
import java.io.IOException;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomLoginFailureHandler customLoginFailureHandler;
    //fail2ban을 활용한 로그기록
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

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
                .formLogin(form -> form
                        .failureHandler(customLoginFailureHandler))

                .httpBasic(AbstractHttpConfigurer::disable) // 기본 로그인 페이지 사용하지 않음
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 보안성 및 확장성 설정
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer
                            .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증(Authentication) 예외 핸들러 지정
                            .accessDeniedHandler(new CustomerAccessDeniedHandler()); // 인가(Authorization) 예외 핸들러 지정
                });

        setAccessTokenFilter(httpSecurity); // 필터 설정
        setPermissions(httpSecurity); // web 보안 접근 설정

        return httpSecurity.build();
    }

    // 인증 관련 Filter 설정 클래스 : JwtAuthenticationFilter 추가
    private void setAccessTokenFilter(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(memberRepository, tokenProvider, customLoginFailureHandler);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 설정(모든 origin 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration conf = new CorsConfiguration();
        //"http://52.79.245.244/"
        conf.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000", "https://ppiyongppiyong.co.kr"));
        conf.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "DELETE"));// 모든 HTTP 메서드 허용
        conf.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf); // 모든 경로에 대하여 설정한 CORS 정책들을 적용

        return source;
    }


    // 인가 설정(경로별 접근 권한 설정)
    private void setPermissions(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/swagger-ui/**","/v3/api-docs/**","/api/v1/**", "/actuator/**").permitAll() // 로그인과 회원가입에 대하여는 누구든 접근 가능
                .anyRequest().authenticated() // 그 외의 모든 요청에 대하여는 권한이 필요
        );
    }




}
