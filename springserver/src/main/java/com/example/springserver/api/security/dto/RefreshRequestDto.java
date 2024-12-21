package com.example.springserver.api.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshRequestDto {
    private String refreshToken;
}
