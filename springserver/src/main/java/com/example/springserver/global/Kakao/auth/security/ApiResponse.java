package com.example.springserver.global.Kakao.auth.security;

import com.example.springserver.global.Kakao.auth.Error.ErrorStatus;

public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    // 생성자, 게터, 세터

    public static ApiResponse of(ErrorStatus errorStatus) {
        ApiResponse response = new ApiResponse();
        response.status = "Error";
        response.message = errorStatus.getMessage();
        response.data = null; // 또는 errorStatus를 바탕으로 더 상세한 정보를 제공할 수 있습니다
        return response;
    }
}
