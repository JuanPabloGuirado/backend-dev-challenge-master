package com.directa24.main.challenge.domain.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String errorCode;

    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}