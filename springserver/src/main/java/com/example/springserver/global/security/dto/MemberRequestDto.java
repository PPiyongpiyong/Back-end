package com.example.springserver.global.security.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Schema
@Builder
public record MemberRequestDto (
        @Schema(name = "이메일", example = "aaa1111@example.com")
        String email,
        @Schema(name = "사용자 이름", example = "홍길동")
        String username,
        @Schema(name = "비밀번호", example = "1234")
        String password,
        @Schema(name = "전화번호", example = "010-1111-1111")
        String phoneNumber,
        @Schema(name = "성별", example = "female")
        String gender,
        @Schema(name = "보호자 전화번호", example = "010-1111-1111")
        String parentPhoneNumber,
        @Schema(name = "거주 주소", example = "서울특별시")
        String address,
        @Schema(name = "주민등록번호", example = "010101-4000000")
        String residentNo
){
}
