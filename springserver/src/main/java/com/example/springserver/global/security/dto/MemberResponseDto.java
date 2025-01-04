package com.example.springserver.global.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
