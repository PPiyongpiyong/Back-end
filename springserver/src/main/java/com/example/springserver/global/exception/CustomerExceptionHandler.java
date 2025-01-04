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

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {

        ErrorCode errorCode = e.getErrorCode();
        log.error("Request URI : [{}] {}", request.getMethod() ,request.getRequestURI()); // request 기록
        log.error("Error message : {}", errorCode.getDescription()); // Custom에서 설정한 description을 기록

        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
