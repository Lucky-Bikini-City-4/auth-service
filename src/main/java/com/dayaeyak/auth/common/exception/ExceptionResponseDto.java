package com.dayaeyak.auth.common.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponseDto(
        HttpStatus status,

        String message
) {
}
