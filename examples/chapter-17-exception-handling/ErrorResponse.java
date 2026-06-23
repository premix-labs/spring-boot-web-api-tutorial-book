package com.example.backendapi.common;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        int status,
        String message,
        String path,
        LocalDateTime timestamp,
        Map<String, String> errors
) {
    public static ErrorResponse of(int status, String message, String path) {
        return new ErrorResponse(false, status, message, path, LocalDateTime.now(), null);
    }

    public static ErrorResponse validation(
            int status,
            String message,
            String path,
            Map<String, String> errors
    ) {
        return new ErrorResponse(false, status, message, path, LocalDateTime.now(), errors);
    }
}

