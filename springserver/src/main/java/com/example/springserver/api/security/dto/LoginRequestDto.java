package com.example.springserver.api.security.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequestDto {
    private String id;
    private String password;
}
