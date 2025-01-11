package com.example.springserver.global.auth;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.springserver.global.security.domain.constants.JwtValidationType.VALID_JWT;

// JWT 인증을 검증하기 위한 클래스
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal( // 전체 프로젝트에 적용할 filter 설정
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT를 추출하기
        String token = getJwtFromRequest(request);

        try {
            // 2. JWT 유효성 검증
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token) == VALID_JWT) {
                Long memberId = tokenProvider.getMemberIdFromToken(token); // 유효한 jwt에 대하여 email 가져오기

                // ID(PK)를 통하여 인증 생성
                MemberEntity member = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

                MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(member);

                // 받은 요청을 기반으로 인증에 대한 detail 입력하기(UserDetails)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"" + e.getMessage() + "\"}");

            return; // 필터 체인을 중단
        }
        filterChain.doFilter(request, response);
    }

    // 요청(Request)에서 JWT 토큰 추출
    // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjM5MDIyfQ.S...
    private String getJwtFromRequest(HttpServletRequest request) {

        // 헤더에서 Authorization 정보를 가져오기
        String bearerToken = request.getHeader(TOKEN_HEADER);

        if (bearerToken != null) {
            String token = bearerToken.startsWith(TOKEN_PREFIX) ? bearerToken.substring(TOKEN_PREFIX.length()) : bearerToken;


            // Authorization이 아닌 경우에 대한 체크
            if (!StringUtils.hasText(token)) {
                log.warn("Authorization header is missing or empty");
                return null;
            }
            log.info("입력받은 토큰 : {" + token + "}");
            return token;
        }

        return null;
    }

    // 특정 경로에는 filter를 적용하지 않도록 설정하는 메소드
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        System.out.println("Request path: " + path); // 경로 로그 출력
        boolean shouldNotFilter = path.startsWith("/auth/signin") ||
                path.startsWith("/auth/signup") ||
                path.startsWith("/auth/account") ||
                path.startsWith("/swagger-ui/index.html");
        System.out.println("Should not filter: " + shouldNotFilter); // 필터링 여부 출력
        return shouldNotFilter;
    }

    // 권한 관련
    private void setAuthentication(HttpServletRequest request, Long memberId) {

        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNT));

        MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(member);

        createWebAuthenticationDetailsAndSet(request, authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    //HTTP 요청에서 인증 상세 정보를 추출하는 클래스
    private void createWebAuthenticationDetailsAndSet(HttpServletRequest request, MemberAuthentication authentication) {
        WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails webAuthenticationDetails = webAuthenticationDetailsSource.buildDetails(request);
        authentication.setDetails(webAuthenticationDetails);
    }

}
