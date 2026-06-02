package com.javacodepoint.email.service;

import com.javacodepoint.email.constant.EmailConstants;
import com.javacodepoint.email.dto.EmailAttachment;
import com.javacodepoint.email.validator.EmailValidatorUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static final Set<String>
            ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/csv"
    );

    @Async("emailExecutor")
    public CompletableFuture<Void> sendEmail(

            String to,

            String cc,

            String bcc,

            String subject,

            String body,

            List<EmailAttachment> attachments) {

        log.info(
                "Starting email send process. Subject={}, To={}",
                subject,
                to
        );

        try {

            validateEmails(
                    to,
                    cc,
                    bcc
            );

            validateAttachments(
                    attachments
            );

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    senderEmail
            );

            helper.setTo(
                    splitEmails(to)
            );

            if (isNotBlank(cc)) {

                helper.setCc(
                        splitEmails(cc)
                );
            }

            if (isNotBlank(bcc)) {

                helper.setBcc(
                        splitEmails(bcc)
                );
            }

            helper.setSubject(
                    subject
            );

            helper.setText(
                    body,
                    true
            );

            addAttachments(
                    helper,
                    attachments
            );

            mailSender.send(
                    mimeMessage
            );

            log.info(
                    "Email sent successfully. Subject={}, To={}",
                    subject,
                    to
            );

            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {

            log.error(
                    "Email sending failed. Subject={}, To={}",
                    subject,
                    to,
                    ex
            );

            return CompletableFuture.failedFuture(ex);
        }
    }

    private void validateEmails(
            String to,
            String cc,
            String bcc) {

        EmailValidatorUtil.validateEmails(to);

        EmailValidatorUtil.validateEmails(cc);

        EmailValidatorUtil.validateEmails(bcc);
    }

    private String[] splitEmails(
            String emails) {

        return Arrays.stream(
                        emails.split(","))
                .map(String::trim)
                .filter(email -> !email.isBlank())
                .toArray(String[]::new);
    }

    private void validateAttachments(
            List<EmailAttachment> attachments) {

        if (attachments == null ||
                attachments.isEmpty()) {
            return;
        }

        for (EmailAttachment attachment :
                attachments) {

            validateFileSize(
                    attachment
            );

            validateFileExtension(
                    attachment
            );

            validateContentType(
                    attachment
            );
        }
    }

    private void validateFileSize(
            EmailAttachment attachment) {

        if (attachment.size()
                > EmailConstants.MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    attachment.fileName()
                            + " exceeds maximum allowed size."
            );
        }
    }

    private void validateFileExtension(
            EmailAttachment attachment) {

        String fileName =
                attachment.fileName();

        int lastDot =
                fileName.lastIndexOf(".");

        if (lastDot == -1) {

            throw new IllegalArgumentException(
                    "File extension missing: "
                            + fileName
            );
        }

        String extension =
                fileName.substring(
                                lastDot + 1)
                        .toLowerCase();

        boolean allowed =
                Arrays.stream(
                                EmailConstants.ALLOWED_EXTENSIONS)
                        .anyMatch(
                                extension::equals);

        if (!allowed) {

            throw new IllegalArgumentException(
                    "Unsupported file type: "
                            + extension
            );
        }
    }

    private void validateContentType(
            EmailAttachment attachment) {

        String contentType =
                attachment.contentType();

        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES.contains(contentType)) {

            throw new IllegalArgumentException(
                    "Unsupported content type: "
                            + contentType
            );
        }
    }

    private void addAttachments(
            MimeMessageHelper helper,
            List<EmailAttachment> attachments)
            throws Exception {

        if (attachments == null ||
                attachments.isEmpty()) {
            return;
        }

        for (EmailAttachment attachment :
                attachments) {

            helper.addAttachment(
                    attachment.fileName(),
                    new ByteArrayResource(
                            attachment.content()
                    )
            );

            log.debug(
                    "Attachment added. Name={}, Size={}",
                    attachment.fileName(),
                    attachment.size()
            );
        }
    }

    private boolean isNotBlank(
            String value) {

        return value != null
                && !value.isBlank();
    }
}