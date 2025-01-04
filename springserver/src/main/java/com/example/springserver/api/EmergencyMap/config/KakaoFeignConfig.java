package com.example.springserver.api.EmergencyMap.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfig {

    @Value("${kakao.api.key}") //bb3a9336059bb3a95671d5b538c2e1fb
    private String kakaoApiKey;

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("Authorization", "KakaoAK " + kakaoApiKey);
            }
        };
    }

}