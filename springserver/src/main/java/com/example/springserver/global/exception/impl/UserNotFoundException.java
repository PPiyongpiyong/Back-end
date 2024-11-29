package com.example.springserver.global.exception.impl;

import com.example.springserver.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "유저를 찾을 수 없습니다.";
    }
}
