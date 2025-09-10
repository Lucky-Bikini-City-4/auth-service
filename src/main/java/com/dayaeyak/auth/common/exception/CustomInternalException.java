package com.dayaeyak.auth.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomInternalException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CustomInternalException(HttpStatus status, String message) {
        super(message);

        this.status = status;
        this.message = message;
    }
}
