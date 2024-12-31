package com.example.springserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 스프링 부트 애플리케이션의 진입점입니다.
 */

@EnableFeignClients
@EnableCaching
@SpringBootApplication
<<<<<<< HEAD
@ComponentScan(basePackages = {"com.example.springserver", "com.example.springserver.external"})
=======
>>>>>>> ec6b56e311fe35edd4a4d62bc7fe97577b4a2e92
public class SpringserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringserverApplication.class, args);
	}

}
