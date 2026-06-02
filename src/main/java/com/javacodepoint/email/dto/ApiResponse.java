package com.javacodepoint.email.dto;

import java.time.LocalDateTime;

/**
 * Standard API response object used across
 * the entire application.
 *
 * This helps frontend receive a consistent
 * JSON response structure.
 */
public record ApiResponse(

        boolean success,

        String message,

        LocalDateTime timestamp

) {
}