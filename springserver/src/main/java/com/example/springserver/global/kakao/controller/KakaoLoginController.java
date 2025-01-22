package com.example.springserver.global.kakao.controller;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import com.example.springserver.api.Mypage.repository.MemberRepository;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.kakao.dto.KakaoUserInfoResponseDto;
import com.example.springserver.global.kakao.service.KakaoService;
import com.example.springserver.global.security.dto.MemberResponseDto;
import com.example.springserver.global.security.dto.TokenDto;
import com.example.springserver.global.security.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "카카오 로그인 API")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final TokenProvider tokenProvider;

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
            로그인 성공시 토큰을 발급합니다.
            구체적인 회원정보는 마이페이지 수정에서 처리합니다.
            """, parameters = @Parameter(name = "Code", description = "카카오로부터 받은 인가 코드"))
    @PostMapping("/callback")
    public ResponseEntity<?> callback(
            String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code).access();

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        MemberEntity member = kakaoService.saveMemberInfo(userInfo);

        TokenDto tokenDto = kakaoService.generateToken(member);

        return ResponseEntity.ok(tokenDto);
    }
}