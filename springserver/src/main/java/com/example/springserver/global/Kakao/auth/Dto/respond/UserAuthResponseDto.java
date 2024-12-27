package com.example.springserver.global.Kakao.auth.Dto.respond;

import com.example.springserver.global.Kakao.auth.Domain.User;

public class UserAuthResponseDto {
    private Token token;
    private User user;


    public UserAuthResponseDto(Token token, User user) {
        this.token = token;
        this.user = user;
    }

    public static UserAuthResponseDto of(Token token, User user) {
        return new UserAuthResponseDto(token, user);
    }


}
