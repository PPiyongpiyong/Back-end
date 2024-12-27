package com.example.springserver.global.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private int status;
    private String message;

    public void addMessage(String message) {
        this.message = this.message + ":" + message;
    }
}
