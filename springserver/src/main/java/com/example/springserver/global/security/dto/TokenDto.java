package com.example.springserver.global.security.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TokenDto (
        String access
){

    //
    public static TokenDto of(String access, String refresh) {
        return TokenDto.builder()
                .access(access)
                .build();
    }
}
