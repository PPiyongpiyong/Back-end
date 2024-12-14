package com.example.springserver.global.Kakao.auth.Error;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}