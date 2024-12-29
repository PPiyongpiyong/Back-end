package com.example.springserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 스프링 부트 애플리케이션의 진입점입니다.
 */


@EnableFeignClients
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.example.springserver")
public class SpringserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringserverApplication.class, args);
	}

}
