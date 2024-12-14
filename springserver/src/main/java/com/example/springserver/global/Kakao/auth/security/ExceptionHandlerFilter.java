package com.example.springserver.global.Kakao.auth.security;

import com.example.springserver.global.Kakao.auth.Error.ErrorStatus;

import com.example.springserver.global.Kakao.auth.Error.KaKaoUnauthorizedException;
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
        } catch (KaKaoUnauthorizedException e) {
            KaKaoUnauthorizedException(response, e);
        } catch (Exception ee) {
            handleException(response);
        }
    }

    private void KaKaoUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        KaKaoUnauthorizedException kue = (KaKaoUnauthorizedException) e;
        ErrorStatus errorStatus = kue.getErrorStatus();
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