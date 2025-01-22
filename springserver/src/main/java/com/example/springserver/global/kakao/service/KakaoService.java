
package com.example.springserver.global.kakao.service;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import com.example.springserver.global.kakao.dto.KakaoTokenResponseDto;
import com.example.springserver.global.kakao.dto.KakaoUserInfoResponseDto;
import com.example.springserver.global.security.dto.MemberMapper;
import com.example.springserver.global.security.dto.MemberRequestDto;
import com.example.springserver.global.security.dto.MemberResponseDto;
import com.example.springserver.global.security.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com"; // 인가 코드 요청, host: kauth.kakao.com
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com"; // 프로필 정보 요청, host: kapi.kakao.com

    @Transactional(readOnly = true)
    public String getLoginUrl() {
        return String.format(
                "%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&prompt=login",
                KAUTH_TOKEN_URL_HOST, clientId, redirectUri);
    }

    @Transactional
    public TokenDto getAccessTokenFromKakao(String code) {
        try {
            KakaoTokenResponseDto response = createWebClient(KAUTH_TOKEN_URL_HOST) // kakao의 경우 kauth.kakao.com
                    .post() // post 방식으로 key-value 타입의 정보들을 카카오 서버로 요청
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/token") // post 요청
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("redirect_uri", redirectUri)
                            .queryParam("code", code)
                            .build())
                    .retrieve()
                    .bodyToMono(KakaoTokenResponseDto.class) // 응답의 형식
                    .block();

            // 요청에 대한 응답 기록(Response)
            logTokenInfo(response);

            String accessToken = response.getAccessToken(); // 응답의 액세스 토큰 부분만 가져오기
            String refreshToken = response.getRefreshToken(); // 응답의 리프레시 토큰 부분만 가져오기
            TokenDto tokenDto = TokenDto.of(accessToken, refreshToken);

            return tokenDto;
        } catch (WebClientResponseException ex) {
            log.error("Error retrieving access token: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve access token", ex);
        }
    }

    // 카카오 프로필 정보 얻어오기
    @Transactional
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        try {
            return createWebClient(KAUTH_USER_URL_HOST) // 프로필 정보에 대한 host: kapi.kakao.com
                    .get() // GET 메소드를 사용
                    .uri("/v2/user/me") // 정보를 얻어오는 주소
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // header
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponseDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error retrieving user info: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve user info", ex);
        }
    }

    // 받은 정보를 바탕으로 사용자 저장
    @Transactional
    public MemberEntity saveMemberInfo(KakaoUserInfoResponseDto userInfo) {

        KakaoUserInfoResponseDto.KakaoAccount account = userInfo.getKakaoAccount();

        // 필요한 정보 확인
        if (account == null) {
            throw new CustomException(ErrorCode.SHOULD_PERMISSION);
        }

        try {
            MemberRequestDto memberDto = MemberRequestDto.builder()
                    .email(account.getEmail())
                    .username(account.getName())
                    .phoneNumber(account.getPhoneNumber().substring(3))
                    .gender(account.getGender())
                    .build();
            MemberEntity member = MemberMapper.toEntity(memberDto);

            // MemberEntity 저장
            if (!memberRepository.findByEmail(member.getEmail()).isPresent()) {
                memberRepository.save(member);
            } else {
                log.info(member.getEmail() + "등록된 유저 정보를 가져옵니다.");
            }

            return member;
        } catch (Exception e){
            throw new CustomException(ErrorCode.SHOULD_PERMISSION);
        }
    }

    // 회원 저장 후 토큰 발급
    @Transactional
    public TokenDto generateToken(MemberEntity member) {

        String access = tokenProvider.generateAccessToken(member);
        String refresh = tokenProvider.generateRefreshToken(member);

        return TokenDto.of(access, refresh);
    }

    private WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl) //요청 body(key-value)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();
    }

    // 요청에 대한 응답 기록
    private void logTokenInfo(KakaoTokenResponseDto tokenResponse) {
        log.info("[Kakao Service] Access Token: {}", tokenResponse.getAccessToken());
        log.info("[Kakao Service] Refresh Token: {}", tokenResponse.getRefreshToken());
        log.info("[Kakao Service] ID Token: {}", tokenResponse.getIdToken());
        log.info("[Kakao Service] Scope: {}", tokenResponse.getScope());
    }
}