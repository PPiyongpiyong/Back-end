package com.example.springserver.api.Manual.exception;

public class ManualNotFoundException extends RuntimeException {
    public ManualNotFoundException(String message) {
        super(message);
    }
}
