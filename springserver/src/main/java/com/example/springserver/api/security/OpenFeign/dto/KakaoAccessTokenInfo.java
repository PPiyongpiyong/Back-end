package com.example.springserver.api.security.OpenFeign.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAccessTokenInfo {
    private Long id;
}
