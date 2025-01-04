package com.example.springserver.global.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;


import java.io.PrintWriter;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 인증에 대한 예외 오류에 대하여 설정
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String accept = request.getHeader("Accept");

        if ("application/json".equals(accept)) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(401)
                    .message("인증이 필요합니다.")
                    .build();

            String result = objectMapper.writeValueAsString(errorResponse); // 작성한 것을 string으로 본문 작성

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(401);
            response.getWriter().write(result);
        }
    }
}