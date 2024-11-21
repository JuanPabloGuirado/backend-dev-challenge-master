package com.directa24.main.challenge.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoviesApiException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus status;

    public MoviesApiException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
