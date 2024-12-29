package com.example.springserver.api.security.dto;


import java.util.List;

public record MemberRequestDto (
        String email,
        String username,
        String password,
        List<String> roles,
        String phoneNumber,
        String gender,
        String parentPhoneNumber,
        String address,
        String residentNo
){
}
