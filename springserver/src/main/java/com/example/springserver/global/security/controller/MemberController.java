package com.example.springserver.global.security.controller;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.dto.LoginRequestDto;
import com.example.springserver.global.security.dto.MemberRequestDto;
import com.example.springserver.global.security.dto.MemberResponseDto;
import com.example.springserver.global.security.dto.RefreshRequestDto;
import com.example.springserver.global.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    // 회원 가입하기
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signUp(
            @RequestBody MemberRequestDto requestDto
            ) {
        return ResponseEntity.ok(
                memberService.register(requestDto)
        );
    }

    // 로그인하기
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @RequestBody LoginRequestDto requestDto
            ) {
        return ResponseEntity.ok(memberService.GeneralLogin(requestDto));
    }

    // Access Token을 재발급받기
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequestDto request
            ) {

        return ResponseEntity.ok(Map.of(
                "accessToken",
                memberService.regenerateAccessToken(request)));

    }
}
