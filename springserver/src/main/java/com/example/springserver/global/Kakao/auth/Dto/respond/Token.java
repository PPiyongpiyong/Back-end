package com.example.springserver.global.Kakao.auth.Dto.respond;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Builder(access = AccessLevel.PRIVATE)
public record Token(
        String accessToken,
        String refreshToken
) {

    public static Token of(String accessToken, String refreshToken) {

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}

