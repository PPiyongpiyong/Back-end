package com.example.springserver.global.kakao.controller;

import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.kakao.dto.KakaoUserInfoResponseDto;
import com.example.springserver.global.kakao.service.KakaoService;
import com.example.springserver.global.security.dto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @Operation(summary = "카카오 인가 코드 받기", description = """
            카카오 인가 코드를 받기 위한 url을 반환합니다.
            """)
    @GetMapping("/page")
    public String page() {
        return kakaoService.getLoginUrl();
    }

    @Operation(summary = "카카오 로그인", description = """
            카카오 로그인을 통하여 유저의 이름과, 이메일을 저장합니다.<br>
            인가 코드 요청 url을 통하여 코드를 받습니다.<br>
            구체적인 회원정보는 마이페이지 수정에서 처리합니다.
            """, parameters = @Parameter(name = "Code", description = "카카오로부터 받은 인가 코드"))
    @PostMapping("/callback")
    public ResponseEntity<?> callback(
            String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code).access();

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        MemberResponseDto responseDto = kakaoService.saveMemberInfo(userInfo);
        return ResponseEntity.ok(responseDto);
    }
}