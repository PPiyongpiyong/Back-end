package com.example.springserver.api.security.controller;

import com.example.springserver.api.security.domain.MemberEntity;
import com.example.springserver.api.security.dto.MemberAuthResponseDto;
import com.example.springserver.api.security.service.MemberService;
import com.example.springserver.api.security.service.OAuth2MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final MemberService memberService;
    private final OAuth2MemberService oAuthService;

    // 카카오 로그인 창으로 redirect
    @GetMapping("/kakao")
    public void getKakaoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuthService.responseUrl());
    }

    // 카카오 로그인 API (성공 후 토큰 발급)
    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(name = "code") String code,
            HttpServletResponse httpServletResponse) throws IOException {
        MemberEntity member =  oAuthService.kakaoLogin(code, httpServletResponse);
        return ResponseEntity.ok(member);
    }
}
