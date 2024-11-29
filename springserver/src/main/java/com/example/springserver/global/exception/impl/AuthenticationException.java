package com.example.springserver.global.exception.impl;

import com.example.springserver.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getMessage() {
        return "인증 정보를 확인하세요.";
    }
}
