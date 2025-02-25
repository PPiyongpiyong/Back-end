package com.example.springserver.global.security.controller;

import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.dto.*;
import com.example.springserver.global.security.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "일반 로그인 API")
public class MemberController {

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
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
            ) {
        return ResponseEntity.ok(memberService.GeneralLogin(requestDto, response));
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
            HttpServletRequest request, HttpServletResponse response
            ) {
        return ResponseEntity.ok(memberService.refresh(request, response));
    }

    // 로그아웃하기
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response,
            @RequestHeader("Authorization") String authToken
    ) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        memberService.logout(response, token);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 회원탈퇴하기
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(
            @RequestHeader("Authorization") String authToken
    ) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;

        memberService.delete(token);

        return ResponseEntity.ok("성공적으로 탈퇴 되었습니다.");
    }

    // 비밀번호 찾기 요청(이메일로 인증번호 전송하기)
    @PostMapping("/password/verification")
    public void verification(
            @RequestBody VerificationRequestDto request
    ) {
        memberService.sendVerificationCode(request);
    }

    // 인증번호 검증받기
    @PostMapping("/certiMail")
    public void verifyMail(
            @RequestBody VerificationRequestDto request,
            @RequestParam String code
    ) {
        memberService.verify(request, code);
    }

    // 인증번호 확인 후 비밀번호 초기화하기
    @PostMapping("/password/reset")
    public ResponseEntity<?> changePassword(
        PasswordRequestDto request
    ) {
        return ResponseEntity.ok(memberService.change(request));
    }
}
