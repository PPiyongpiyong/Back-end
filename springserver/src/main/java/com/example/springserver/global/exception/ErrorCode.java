package com.example.springserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 공통 에러
    UNAUTHORIZED(401, "인증되지 않았습니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    USER_NOT_FOUNT(400, "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(400, "이미 존재하는 사용자입니다."),
    UNMATCHED_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    FORBIDDEN(403, "권한이 금지되었습니다."),
    REFRESH_TOKEN_UNMATCHED(403, "refresh 토큰이 일치하지 않습니다."),
    // 커스텀 에러
    MANUAL_NOT_FOUND(400, "매뉴얼을 찾을 수 없습니다.");
    ;

    private final int status;
    private final String description;
}
