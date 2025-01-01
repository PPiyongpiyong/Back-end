package com.example.springserver.global.security.controller;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.dto.LoginRequestDto;
import com.example.springserver.global.security.dto.MemberRequestDto;
import com.example.springserver.global.security.dto.MemberResponseDto;
import com.example.springserver.global.security.dto.RefreshRequestDto;
import com.example.springserver.global.security.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "회원 가입하기", description = "회원 가입 API 입니다. ", requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원 가입 요청 객체",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MemberRequestDto.class)
                    )
            )
    )
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
