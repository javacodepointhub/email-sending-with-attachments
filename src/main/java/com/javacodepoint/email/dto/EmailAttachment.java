package com.javacodepoint.email.dto;

/**
 * DTO used for passing attachment data
 * safely across async threads.
 */
public record EmailAttachment(

        String fileName,

        byte[] content,

        long size,

        String contentType

) {
}