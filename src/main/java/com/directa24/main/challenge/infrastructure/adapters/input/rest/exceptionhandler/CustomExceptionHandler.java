package com.directa24.main.challenge.infrastructure.adapters.input.rest.exceptionhandler;

import com.directa24.main.challenge.domain.exception.ErrorResponse;
import com.directa24.main.challenge.domain.exception.MoviesApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MoviesApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(MoviesApiException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", "GENERIC_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid query parameter: " + ex.getName(),
                "TYPE_MISMATCH");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
