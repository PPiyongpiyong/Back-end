package com.example.springserver.global.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshRequestDto {
    private String accessToken;
}
