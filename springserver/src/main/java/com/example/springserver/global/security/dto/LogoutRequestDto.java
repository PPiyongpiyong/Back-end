package com.example.springserver.global.security.dto;

import lombok.Getter;

@Getter
public class LogoutRequestDto {
    private String email;
    private String password;
}
