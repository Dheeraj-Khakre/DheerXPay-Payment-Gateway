package com.dheerx.DheerX_Pay.common.api;

import java.time.Instant;

/**
 * Standard API response wrapper used across the payment gateway services.
 *
 * @param <T> payload type returned by the API
 */
public record ApiResponse<T>(
        String status,
        String message,
        T data,
        Instant timestamp
) {
    public ApiResponse(String status, String message, T data) {
        this(status, message, data, Instant.now());
    }
}

