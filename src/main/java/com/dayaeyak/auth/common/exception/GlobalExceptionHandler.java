package com.dayaeyak.auth.common.exception;

import com.dayaeyak.auth.common.entity.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiResponse<ExceptionResponseDto>> handleCustomException(CustomRuntimeException e) {
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(CustomInternalException.class)
    public ResponseEntity<ApiResponse<ExceptionResponseDto>> handleCustomFeignException(CustomInternalException e) {
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }
}
