package com.example.springserver.api.security.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
