package com.example.springserver.global.kakao.controller;

import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.kakao.dto.KakaoUserInfoResponseDto;
import com.example.springserver.global.kakao.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;

    @Operation(summary = "카카오 로그인")
    @GetMapping("/callback")
    public String callback(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code).access();

        kakaoService.getUserInfo(code);
        return "access_token=" + accessToken;
    }
}