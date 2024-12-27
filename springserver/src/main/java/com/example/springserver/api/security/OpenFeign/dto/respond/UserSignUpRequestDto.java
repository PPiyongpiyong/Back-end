package com.example.springserver.api.security.OpenFeign.dto.respond;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record UserSignUpRequestDto(String nickname) {
}
