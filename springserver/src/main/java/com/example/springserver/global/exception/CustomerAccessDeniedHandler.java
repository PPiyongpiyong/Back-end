package com.example.springserver.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    // AccessDeniedHandler에서의 메소드 위임
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 인가에 대한 예외 처리
        log.error("Access denied: [{}] {}", request.getMethod(), request.getRequestURI());
        log.error("Error message: {}", accessDeniedException.getMessage());

        // JSON 형식으로 에러 응답
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(403)
                .message("접근 권한이 없습니다.")
                .build();

        String result = objectMapper.writeValueAsString(errorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403); // 인증에는 성공하였으나, 인가 실패에 대한 코드
        response.getWriter().write(result);
    }
}