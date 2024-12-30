package com.example.springserver.global.security.dto;

import com.example.springserver.api.Mypage.domain.MemberEntity;
import lombok.Data;

@Data
public class LoginResponseDto {
    private MemberEntity member;
    private String token;

    public LoginResponseDto(MemberEntity member, String token) {
        this.member = member;
        this.token = token;
    }
}
