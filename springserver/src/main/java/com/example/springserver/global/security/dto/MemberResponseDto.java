package com.example.springserver.global.security.dto;

public record MemberResponseDto(
        String email,
        String username,
        String phoneNumber,
        String gender,
        String parentPhoneNumber,
        String address,
        String residentNo
) {
}
