package com.example.springserver.api.security.dto;

import com.example.springserver.api.security.domain.MemberEntity;
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
