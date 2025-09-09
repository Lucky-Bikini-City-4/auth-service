package com.dayaeyak.auth.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType implements ExceptionType {

    USER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "유저 서비스가 동작하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
