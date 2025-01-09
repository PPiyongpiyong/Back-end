package com.example.springserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 공통 에러
    UNAUTHORIZED(401, "인증되지 않았습니다."),
    INVALID_ACCESS_TOKEN(401, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_ACCESS_TOKEN_VALUE(401, "액세스 토큰 값이 올바르지 않습니다."),
    TOKEN_UNEXPIRED(401, "액세스 토큰이 만료되지 않았습니다. 유효한 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(401, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    FORBIDDEN(403, "권한이 금지되었습니다."),
    REFRESH_TOKEN_UNMATCHED(403, "refresh 토큰이 일치하지 않습니다."),
    INVALID_KAKAO_ACCESS_TOKEN(401, "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다."),
    // 커스텀 에러
    MANUAL_NOT_FOUND(404, "매뉴얼을 찾을 수 없습니다."),
    USER_NOT_FOUNT(404, "사용자를 찾을 수 없습니다"),
    UNMATCHED_PASSWORD(404, "비밀번호가 일치하지 않습니다."),
    _PARSING_ERROR(404, "찾을 수 없습니다."),
    SHOULD_PERMISSION(404, "동의를 진행해주세요."),
    // 409 Conflict
    USER_ALREADY_EXISTS(409, "이미 존재하는 사용자입니다."),
    DUPLICATED_ID(409, "이미 존재하는 아이디입니다."),
    // 500
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다.")
    ;

    private final int status;
    private final String description;
}
