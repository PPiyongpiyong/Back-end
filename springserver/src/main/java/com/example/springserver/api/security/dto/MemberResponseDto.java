package com.example.springserver.api.security.dto;

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
