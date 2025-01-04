package com.example.springserver.api.Mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record MypageReqeustDto (
        // 수정 가능한 내용들
        @Schema(description = "사용자 이름", example = "홍길동")
        String username,
        @Schema(description = "주소", example = "서울특별시")
        String address,
        @Schema(description = "사용자 전화번호", example = "010-1111-1111")
        String phoneNumber,
        @Schema(description = "사용자 보호자 전화번호", example = "010-1111-1111")
        String parentPhoneNumber
){
}
