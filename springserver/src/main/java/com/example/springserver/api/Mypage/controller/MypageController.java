package com.example.springserver.api.Mypage.controller;

import com.example.springserver.api.Mypage.dto.MypageReqeustDto;
import com.example.springserver.api.Mypage.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Mypage", description = "마이페이지 관련 API")
public class MypageController {

    private final MypageService mypageService;


    @Operation(summary = "회원 정보 수정하기", description = """
            회원 정보를 수정합니다.<br>
            헤더에 accessToken을 넣어주세요.
            """, parameters = {@Parameter(name = "MypageRequest", description = "회원가입 요청 객체")})
    @PutMapping("/mypage/updateProfile")
    public ResponseEntity<?> updateProfile(
            @RequestBody MypageReqeustDto requestDto,
            @RequestHeader("Authorization") String authToken

    ) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(mypageService.addMemberInfo(token, requestDto));
    }


    @Operation(summary = "마이페이지 조회하기", description = """
            마이페이지를 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.
            """)
    @GetMapping("/mypage/getProfile")
    public ResponseEntity<?> getProfile(
            @RequestHeader("Authorization") String authToken
    ) {
        authToken = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(mypageService.getMyInfo(authToken));
    }
}
