package com.example.springserver.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

// Filter에서 오류가 생길 시 연결되는 클래스
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            unauthorizedException(response, e);
        } catch (Exception ee) {
            handleException(response);
        }
    }

    // 권한 불가
    private ResponseEntity<ErrorResponse> unauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        log.error("Unauthorized Exception", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("접근 권한이 없습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }


    private void handleException(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                        .build();
    }

}