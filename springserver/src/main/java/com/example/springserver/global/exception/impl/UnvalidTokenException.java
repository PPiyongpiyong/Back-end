package com.example.springserver.global.exception.impl;

import com.example.springserver.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnvalidTokenException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getMessage() {
        return "유효하지 않은 토큰입니다.";
    }
}
