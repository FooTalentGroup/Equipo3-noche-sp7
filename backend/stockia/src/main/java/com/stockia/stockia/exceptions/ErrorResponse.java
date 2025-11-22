package com.stockia.stockia.exceptions;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        int statusCode,
        String errorCode,
        String message,
        List<String> details,
        String timestamp,
        String path
) {
    public ErrorResponse(int statusCode, String errorCode, String message,
            List<String> details, String path) {
        this(statusCode, errorCode, message, details, Instant.now().toString(), path);
    }
}
