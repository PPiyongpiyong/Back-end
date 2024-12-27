package com.example.springserver.api.security.dto;

public record TokenDto (
        String access,
        String refresh
){
}
