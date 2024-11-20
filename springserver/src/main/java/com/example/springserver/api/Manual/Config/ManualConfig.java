package com.example.springserver.api.Manual.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ManualConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //어떤 URI로 들어오는 요청을 허용할 것인가?
                .allowedOrigins("*") // 모든 도메인에서의 요청을 허용
                .allowedMethods("GET")
                .allowedHeaders("*"); // 모든 헤더를 허용
    }
}