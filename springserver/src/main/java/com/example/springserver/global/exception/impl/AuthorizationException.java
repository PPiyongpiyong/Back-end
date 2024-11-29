package com.example.springserver.global.exception.impl;

import com.example.springserver.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthorizationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getMessage() {
        return "권한을 확인하세요.";
    }
}
