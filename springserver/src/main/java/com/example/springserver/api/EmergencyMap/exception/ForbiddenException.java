package com.example.springserver.api.EmergencyMap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}