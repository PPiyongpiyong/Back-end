package com.example.springserver.global.security.controller;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.domain.constants.JwtValidationType;
import com.example.springserver.global.security.dto.LoginRequestDto;
import com.example.springserver.global.security.dto.MemberRequestDto;
import com.example.springserver.global.security.dto.MemberResponseDto;
import com.example.springserver.global.security.dto.RefreshRequestDto;
import com.example.springserver.global.security.service.MemberService;
import io.jsonwebtoken.Claims;
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

    @Operation(summary = "로그인하기", description = "로그인 기능의 API입니다.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "아이디(이메일 주소)와 비밀번호",
                        required = true,
                        content = @Content(
                                schema = @Schema(implementation = LoginRequestDto.class)
                        )
                ))
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @RequestBody LoginRequestDto requestDto
            ) {
        return ResponseEntity.ok(memberService.GeneralLogin(requestDto));
    }

    // Access Token을 재발급받기
    @Operation(summary = "액세스 토큰 재발급", description = """
            access token이 만료되면, refresh token의 유효성을 판단해 다시 access token을 발급받습니다.
            """, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "만료된 액세스 토큰과 리프레시 토큰",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshRequestDto.class)
                    )
    ))
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequestDto request
            ) {
        return ResponseEntity.ok(memberService.refresh(request));
    }
}
