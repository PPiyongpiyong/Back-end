package com.example.springserver.global.exception.impl;

public class ManualNotFoundException extends RuntimeException {
    public ManualNotFoundException(String message) {
        super(message);
    }
}
