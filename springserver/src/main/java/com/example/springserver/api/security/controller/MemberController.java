package com.example.springserver.api.security.controller;

import com.example.springserver.api.security.auth.TokenProvider;
import com.example.springserver.api.security.dto.LoginRequestDto;
import com.example.springserver.api.security.dto.MemberRequestDto;
import com.example.springserver.api.security.dto.RefreshRequestDto;
import com.example.springserver.api.security.dto.TokenDto;
import com.example.springserver.api.security.repository.MemberRepository;
import com.example.springserver.api.security.service.MemberService;
import com.example.springserver.global.exception.CustomException;
import com.example.springserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.springserver.api.security.domain.constants.JwtValidationType.VALID_JWT;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final TokenProvider tokenProvider;
    MemberService memberService;
    MemberRepository memberRepository;

    // 회원 가입하기
    @PostMapping("/auth/signup")
    public ResponseEntity<TokenDto> signUp(
            @RequestBody MemberRequestDto requestDto
            ) {
        return ResponseEntity.ok(
                memberService.register(requestDto)
        );
    }

    // 로그인하기
    @PostMapping("/auth/signin")
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
                "accessTooken",
                memberService.regenerateAccessToken(request)));

    }

    // 회원 정보 수정하기
    @PostMapping("/mypage/updateProfile")
    public ResponseEntity<?> updateProfile(
            @RequestBody MemberRequestDto requestDto,
            @RequestHeader("Authorization") String authToken
    ) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(memberService.addMemberInfo(token, requestDto));
    }

    // 나의 정보 조회하기
    @GetMapping("/mypage/{memberId}/getProfile")
    public ResponseEntity<?> getProfile(
            @RequestHeader("Authorization") String authToken
    ) {
        authToken = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(memberService.getMyInfo(authToken));
    }
}
