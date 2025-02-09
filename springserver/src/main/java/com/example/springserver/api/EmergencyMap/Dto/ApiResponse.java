package com.example.springserver.api.EmergencyMap.Dto;

import io.netty.handler.codec.http.HttpScheme;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;


@Getter
public class ApiResponse <T> {
    private final int statusCode;
    private final String message;
    private final T data;

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), data);

    }

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, Optional<T> data) {
        return new ApiResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), data.orElse(null));
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.value(), message, null);
    }
}