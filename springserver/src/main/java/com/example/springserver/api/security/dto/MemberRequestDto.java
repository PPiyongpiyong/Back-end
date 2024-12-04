package com.example.springserver.api.security.dto;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

public record MemberRequestDto (
        String id,
        String password,
        List<String> roles,
        String phoneNumber,
        String gender,
        String parentPhoneNumber,
        String address,
        String residentNo
){
}
