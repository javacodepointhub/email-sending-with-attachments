package com.javacodepoint.email.exception;

import com.javacodepoint.email.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler.
 *
 * Handles all application exceptions
 * from a central location.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles Bean Validation errors.
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        log.warn(
                "Validation Error : {}",
                errorMessage);

        return ResponseEntity
                .badRequest()
                .body(
                        new ApiResponse(
                                false,
                                errorMessage,
                                LocalDateTime.now()
                        )
                );
    }

    /**
     * Handles custom validation errors.
     */
    @ExceptionHandler(
            IllegalArgumentException.class)
    public ResponseEntity<ApiResponse>
    handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.warn(
                "Invalid Request : {}",
                ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(
                        new ApiResponse(
                                false,
                                ex.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    /**
     * Handles unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse>
    handleGenericException(
            Exception ex) {

        log.error(
                "Unexpected Error",
                ex);

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ApiResponse(
                                false,
                                "Unexpected server error occurred.",
                                LocalDateTime.now()
                        )
                );
    }
}