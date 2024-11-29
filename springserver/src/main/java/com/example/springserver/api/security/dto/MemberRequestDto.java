package com.example.springserver.api.security.dto;


public record MemberRequestDto (
        String id,
        String password,
        String phoneNumber,
        String gender,
        String parentPhoneNumber,
        String address,
        String residentNo
){
}
