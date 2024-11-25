package com.example.springserver.api.EmergencyMap.exception;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class KakaoErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            return new IllegalArgumentException("잘못된 요청입니다. 파라미터를 확인해주세요.");
        } else if (response.status() == 401) {
            return new UnauthorizedException("인증에 실패했습니다. API 키를 확인해주세요.");
        } else if (response.status() == 403) {
            return new ForbiddenException("요청이 금지되었습니다. API 사용 한도를 초과했을 수 있습니다.");
        } else if (response.status() == 500) {
            return new RuntimeException("카카오 API 서버에서 오류가 발생했습니다.");
        }


        return defaultErrorDecoder.decode(methodKey, response);
    }

}