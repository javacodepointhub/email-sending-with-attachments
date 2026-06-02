package com.javacodepoint.email.validator;

import java.util.regex.Pattern;

/**
 * Utility class responsible for
 * validating email addresses.
 */
public final class EmailValidatorUtil {

    private EmailValidatorUtil() {
    }

    /**
     * RFC-style email validation pattern.
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );

    /**
     * Validates comma-separated emails.
     *
     * Example:
     * abc@gmail.com,xyz@gmail.com
     */
    public static void validateEmails(
            String emails) {

        if (emails == null ||
                emails.isBlank()) {

            return;
        }

        String[] emailArray =
                emails.split(",");

        for (String email : emailArray) {

            String trimmedEmail =
                    email.trim();

            if (!EMAIL_PATTERN
                    .matcher(trimmedEmail)
                    .matches()) {

                throw new IllegalArgumentException(
                        "Invalid email address: "
                                + trimmedEmail
                );
            }
        }
    }
}