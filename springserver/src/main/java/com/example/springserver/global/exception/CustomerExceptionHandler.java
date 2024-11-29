package com.example.springserver.global.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;

@Slf4j
@ControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                        .code(e.getStatusCode())
                                        .message(e.getMessage())
                                        .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                .code(HttpStatus.FORBIDDEN.value())
                                                .message(e.getMessage())
                                                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(errorResponse);
    }
}
