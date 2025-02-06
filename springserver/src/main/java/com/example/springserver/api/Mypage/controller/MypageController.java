package com.example.springserver.api.Mypage.controller;

import com.example.springserver.api.EmergencyMap.Dto.ApiResponse;
import com.example.springserver.api.EmergencyMap.Dto.HospitalInfo;
import com.example.springserver.api.EmergencyMap.Service.LikeBoardService;
import com.example.springserver.api.Mypage.dto.MypageReqeustDto;
import com.example.springserver.api.Mypage.service.MypageService;
import com.example.springserver.global.auth.TokenProvider;
import com.example.springserver.global.security.domain.constants.JwtValidationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Mypage", description = "마이페이지 관련 API")
public class MypageController {

    private final MypageService mypageService;
    private final LikeBoardService likeBoardService;
    private final TokenProvider tokenProvider;


    @Operation(summary = "회원 정보 수정하기", description = """
            회원 정보를 수정합니다.<br>
            헤더에 accessToken을 넣어주세요.
            """, parameters = {@Parameter(name = "MypageRequest", description = "회원가입 요청 객체",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MypageReqeustDto.class)
            ))})
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


    @Operation(summary = "맵 좋아요 조회하기", description = """
            맵 좋아요를 조회합니다.<br>
            헤더에 accessToken을 넣어주세요.
            """)
    @GetMapping("/mypage/liked")
    public ResponseEntity<ApiResponse<List<HospitalInfo>>> getLikedHospitals(@RequestHeader("Authorization") String authToken) {
        return likeBoardService.getLikedHospitals(authToken);
    }




}
