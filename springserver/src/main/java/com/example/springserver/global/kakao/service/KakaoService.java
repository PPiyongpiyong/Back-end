
/*package com.example.springserver.global.kakao.service;

import com.example.springserver.global.kakao.dto.KakaoTokenResponseDto;
import com.example.springserver.global.kakao.dto.KakaoUserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.api.key}")
    private String restApiKey;

    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com"; // 인가 코드 요청, host: kauth.kakao.com
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public String getLoginUrl() {
        return String.format(
                "%s/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                KAUTH_TOKEN_URL_HOST, restApiKey, redirectUri);
    }

    public String getAccessTokenFromKakao(String code) {
        try {
            KakaoTokenResponseDto tokenResponse = createWebClient(KAUTH_TOKEN_URL_HOST) // kakao의 경우 kauth.kakao.com
                    .post() // post 방식으로 key-value 타입의 정보들을 카카오 서버로 요청
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("redirect_uri", redirectUri)
                            .queryParam("code", code)
                            .build())
                    .retrieve()
                    .bodyToMono(KakaoTokenResponseDto.class)
                    .block();

            logTokenInfo(tokenResponse);

            return tokenResponse.getAccessToken();
        } catch (WebClientResponseException ex) {
            log.error("Error retrieving access token: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve access token", ex);
        }
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        try {
            return createWebClient(KAUTH_USER_URL_HOST)
                    .get()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponseDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error retrieving user info: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve user info", ex);
        }
    }

    private WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .build();
    }

    private void logTokenInfo(KakaoTokenResponseDto tokenResponse) {
        log.info("[Kakao Service] Access Token: {}", tokenResponse.getAccessToken());
        log.info("[Kakao Service] Refresh Token: {}", tokenResponse.getRefreshToken());
        log.info("[Kakao Service] ID Token: {}", tokenResponse.getIdToken());
        log.info("[Kakao Service] Scope: {}", tokenResponse.getScope());
    }
}*/