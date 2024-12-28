package com.example.springserver.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = Appendable.class)
@Configuration
public class FeignClientConfig {
}