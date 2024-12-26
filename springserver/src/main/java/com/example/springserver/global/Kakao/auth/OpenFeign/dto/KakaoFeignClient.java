package com.example.springserver.global.Kakao.auth.OpenFeign.dto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-feign-client", url = "https://kauth.kakao.com/oauth/authorize?client_id=4536a449876cd395c224d92615168ab3&redirect_uri=http://localhost:8080/oauth/code/ppiyong&response_type=code")
public interface KakaoFeignClient {
    @GetMapping("v1/user/access_token_info")
    KakaoAccessTokenInfo getKakaoAccessTokenInfo(@RequestHeader("Authorization") String accessToken);
}
