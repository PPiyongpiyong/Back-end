package com.example.springserver.global.exception.impl;

public class DetailNotFoundException extends RuntimeException {
    public DetailNotFoundException(String message) {
        super(message);
    }
}

