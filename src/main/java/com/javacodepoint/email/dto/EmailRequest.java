package com.javacodepoint.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO used for email submission.
 *
 * Supports:
 * - Multiple TO emails
 * - Multiple CC emails
 * - Multiple BCC emails
 * - HTML Email Body
 */
@Getter
@Setter
public class EmailRequest {

    /**
     * Comma separated email addresses.
     *
     * Example:
     * user1@gmail.com,user2@gmail.com
     */
    @NotBlank(message = "To email address is required")
    private String to;

    /**
     * Comma separated CC emails.
     */
    private String cc;

    /**
     * Comma separated BCC emails.
     */
    private String bcc;

    /**
     * Email Subject
     */
    @NotBlank(message = "Email subject is required")
    private String subject;

    /**
     * Rich HTML content
     */
    @NotBlank(message = "Email body is required")
    private String body;
}