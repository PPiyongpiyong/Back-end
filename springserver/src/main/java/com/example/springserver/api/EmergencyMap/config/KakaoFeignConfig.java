package com.example.springserver.api.EmergencyMap.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfig {

    @Value("${kakao.api.key}")
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