package com.atlas.marketplace.shared;

import java.util.List;

public record ApiError(ErrorBody error) {
    public static ApiError of(String code, String message, String correlationId) {
        return new ApiError(new ErrorBody(code, message, correlationId, List.of()));
    }
    public record ErrorBody(String code, String message, String correlationId, List<FieldError> fieldErrors) {}
    public record FieldError(String field, String message) {}
}
