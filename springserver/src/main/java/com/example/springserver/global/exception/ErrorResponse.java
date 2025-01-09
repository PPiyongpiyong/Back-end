package com.example.springserver.global.exception;

import feign.Response;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Builder
@Data
public class ErrorResponse {
    private int status;
    private String message;

    public void addMessage(String message) {
        this.message = this.message + ":" + message;
    }

    // ResponseBody 설정
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e) {
        return ResponseEntity
                .status(e.getStatus()) // HttpStatus인 ErrorCode (ex. 401)
                .body(ErrorResponse.builder()
                        .status(e.getStatus())
                        .message(e.getDescription())
                        .build()
                );
    }
}
