package com.example.springserver.global.Kakao.auth.Error;

public class KaKaoUnauthorizedException extends BusinessException {
    public KaKaoUnauthorizedException() {
        super(ErrorStatus.UNAUTHORIZED);
    }

    public KaKaoUnauthorizedException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}