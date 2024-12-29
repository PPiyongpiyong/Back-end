package com.example.springserver.api.Mypage.controller;

import com.example.springserver.api.Mypage.dto.MypageReqeustDto;
import com.example.springserver.api.Mypage.service.MypageService;
import com.example.springserver.api.security.dto.MemberRequestDto;
import com.example.springserver.api.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    // 회원 정보 수정하기
    @PutMapping("/mypage/updateProfile")
    public ResponseEntity<?> updateProfile(
            @RequestBody MypageReqeustDto requestDto,
            @RequestHeader("Authorization") String authToken

    ) {
        String token = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(mypageService.addMemberInfo(token, requestDto));
    }

    // 나의 정보 조회하기
    @GetMapping("/mypage/getProfile")
    public ResponseEntity<?> getProfile(
            @RequestHeader("Authorization") String authToken
    ) {
        authToken = authToken.startsWith("Bearer ") ?
                authToken.substring(7) : authToken;
        return ResponseEntity.ok(mypageService.getMyInfo(authToken));
    }
}
