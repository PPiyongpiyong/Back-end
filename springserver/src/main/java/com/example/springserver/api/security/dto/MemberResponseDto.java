package com.example.springserver.api.security.dto;

public record MemberResponseDto(
        String id,
        String name,
        String phoneNumber,
        String gender,
        String parentPhoneNumber,
        String address,
        String residentNo
) {
}
