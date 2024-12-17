package com.example.springserver.global.Kakao.auth.Error;



public class ConflictException extends RuntimeException {
    public ConflictException(ErrorStatus message) {
        super(String.valueOf(message));
    }
}
