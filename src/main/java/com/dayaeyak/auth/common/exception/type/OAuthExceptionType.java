package com.dayaeyak.auth.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuthExceptionType implements ExceptionType{

    FAIL_TO_CALL_GOOGLE_SERVER(HttpStatus.CONFLICT, "구글 서버 통신에 실패하였습니다."),
    FAIL_TO_CALL_KAKAO_SERVER(HttpStatus.CONFLICT, "카카오 서버 통신에 실패하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
