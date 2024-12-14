package com.example.springserver.global.Kakao.auth.security;

import ch.qos.logback.core.status.ErrorStatus;
import com.example.springserver.api.EmergencyMap.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.io.PrintWriter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(response, e);
        } catch (Exception ee) {
            handleException(response);
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        UnauthorizedException ue = (UnauthorizedException) e;
        ErrorStatus errorStatus = ue.getErrorStatus();
        HttpStatus httpStatus = errorStatus.getHttpStatus();
        setResponse(response, httpStatus, errorStatus);
    }

    private void handleException(HttpServletResponse response) throws IOException {
        setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorStatus.INTERNAL_SERVER_ERROR);
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus, ErrorStatus errorStatus) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.CHARACTER_TYPE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ApiResponse.of(errorStatus)));
    }
}