package com.example.springserver.global.Kakao.auth.OpenFeign.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAccessTokenInfo {
    private Long id;
}
