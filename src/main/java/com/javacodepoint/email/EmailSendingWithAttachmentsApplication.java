package com.javacodepoint.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Email Sending Application.
 *
 * This application allows users to:
 * - Send HTML emails
 * - Add multiple recipients
 * - Add CC/BCC recipients
 * - Upload multiple attachments
 */
@SpringBootApplication
public class EmailSendingWithAttachmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSendingWithAttachmentsApplication.class, args);
	}

}
