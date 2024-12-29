package com.example.springserver.api.security.dto;

import com.example.springserver.api.security.domain.MemberEntity;

public class MemberMapper {

    public static MemberEntity toEntity(MemberRequestDto requestDto) {
        return MemberEntity.builder()
                .email(requestDto.email())
                .phoneNumber(requestDto.phoneNumber())
                .username(requestDto.username())
                .roles(requestDto.roles())
                .gender(requestDto.gender())
                .parentPhoneNumber(requestDto.parentPhoneNumber())
                .address(requestDto.address())
                .residentNo(requestDto.residentNo())
                .build();
    }

    public static MemberResponseDto toDto(MemberEntity member) {
        return new MemberResponseDto(
                member.getEmail(),
                member.getUsername(),
                member.getPhoneNumber(),
                member.getGender(),
                member.getParentPhoneNumber(),
                member.getAddress(),
                member.getResidentNo()
        );
    }
}
