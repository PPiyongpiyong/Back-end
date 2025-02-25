package com.example.springserver.global.security.dto;

public record PasswordRequestDto (
        String email,
        String password
){
}
