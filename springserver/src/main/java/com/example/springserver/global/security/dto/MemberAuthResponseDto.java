package com.example.springserver.global.security.dto;

import com.example.springserver.api.Mypage.domain.MemberEntity;

public class MemberAuthResponseDto {
    private TokenDto tokenDto;
    private MemberEntity member;

    public MemberAuthResponseDto(TokenDto tokenDto, MemberEntity member) {
        this.tokenDto = tokenDto;
        this.member = member;
    }

    public static MemberAuthResponseDto of(TokenDto tokenDto, MemberEntity member) {
        return new MemberAuthResponseDto(tokenDto, member);
    }
}
