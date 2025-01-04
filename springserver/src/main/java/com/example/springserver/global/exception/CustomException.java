package com.example.springserver.global.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {
    private int status;
    private ErrorCode errorCode;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
