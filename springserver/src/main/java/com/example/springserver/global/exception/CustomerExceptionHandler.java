package com.example.springserver.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class CustomerExceptionHandler {

    private final View error;

    public CustomerExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {

        ErrorCode errorCode = e.getErrorCode();
        log.error("Request URI : [{}] {}", request.getMethod() ,request.getRequestURI());
        log.error("Error message : {}", errorCode.getDescription());

        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                .status(HttpStatus.FORBIDDEN.value())
                                                .message(e.getMessage())
                                                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(errorResponse);
    }
}
