package com.example.backendapi.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        String code,
        String message,
        Map<String, String> errors,
        Instant timestamp
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(false, code, message, Map.of(), Instant.now());
    }

    public static ErrorResponse withErrors(String code, String message, Map<String, String> errors) {
        return new ErrorResponse(false, code, message, errors, Instant.now());
    }
}
