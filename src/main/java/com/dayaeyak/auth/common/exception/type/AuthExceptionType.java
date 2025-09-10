package com.dayaeyak.auth.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionType implements ExceptionType {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 잘못되었습니다."),
    INVALID_USER_ROLE(HttpStatus.UNAUTHORIZED, "유효하지 않은 유저 권한입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
