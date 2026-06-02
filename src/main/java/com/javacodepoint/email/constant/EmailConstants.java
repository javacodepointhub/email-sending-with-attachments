package com.javacodepoint.email.constant;

/**
 * Application-wide constants.
 */
public final class EmailConstants {

    private EmailConstants() {
    }

    /**
     * Maximum attachment size.
     *
     * 10 MB
     */
    public static final long MAX_FILE_SIZE =
            10 * 1024 * 1024;

    /**
     * Allowed file extensions.
     */
    public static final String[] ALLOWED_EXTENSIONS = {
            "pdf",
            "doc",
            "docx",
            "xls",
            "xlsx",
            "csv",
            "txt",
            "png",
            "jpg",
            "jpeg"
    };
}