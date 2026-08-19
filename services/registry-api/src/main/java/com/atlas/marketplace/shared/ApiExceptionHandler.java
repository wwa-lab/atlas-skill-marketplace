package com.atlas.marketplace.shared;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiError> api(ApiException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.status()).body(ApiError.of(ex.code(), ex.getMessage(), correlation(request)));
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ApiError> invalid(Exception ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ApiError.of("INVALID_REQUEST", "The request is invalid.", correlation(request)));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of("INTERNAL_ERROR", "The request could not be completed.", correlation(request)));
    }
    private static String correlation(HttpServletRequest request) {
        Object value = request.getAttribute(CorrelationFilter.ATTRIBUTE);
        return value == null ? "unavailable" : value.toString();
    }
}
