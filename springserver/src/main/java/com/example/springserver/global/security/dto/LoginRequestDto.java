package com.example.springserver.global.security.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
