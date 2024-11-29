package com.example.springserver.api.security.auth;

import com.example.springserver.api.security.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

import static com.example.springserver.api.security.domain.constants.JwtValidationType.VALID_JWT;

// JWT 인증을 검증하기 위한 클래스
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT를 추출하기
        String token = getJwtFromRequest(request);

        try {
            // 2. JWT 유효성 검증
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token) == VALID_JWT) {
                String memberId = tokenProvider.getMemberIdFromToken(token); // 유효한 jwt에 대하여 id 가져오기

                // ID를 통하여 인증 생성
                MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(memberId);

                // 받은 요청을 기반으로 인증에 대한 detail 입력하기(UserDetails)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return; // 필터 체인을 중단
        }
        filterChain.doFilter(request, response);
    }

    // 요청(Request)에서 JWT 토큰 추출
    // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjM5MDIyfQ.S...
    private String getJwtFromRequest(HttpServletRequest request) {
        // 헤더에서 Authorization 정보를 가져오기
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Bearer  이후만 반환하게 substr
        }
        return null;
    }
}
