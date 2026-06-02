package com.javacodepoint.email.controller;

import com.javacodepoint.email.dto.ApiResponse;
import com.javacodepoint.email.dto.EmailAttachment;
import com.javacodepoint.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendEmail(

            @RequestParam String to,

            @RequestParam(required = false)
            String cc,

            @RequestParam(required = false)
            String bcc,

            @RequestParam String subject,

            @RequestParam String body,

            @RequestParam(required = false)
            MultipartFile[] attachments) throws IOException {

        log.info(
                "Email request received. Subject={}, To={}",
                subject,
                to
        );

        List<EmailAttachment> emailAttachments =
                buildAttachments(attachments);

        emailService.sendEmail(
                to,
                cc,
                bcc,
                subject,
                body,
                emailAttachments
        );

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Email request submitted successfully.",
                        LocalDateTime.now()
                )
        );
    }

    /**
     * Convert MultipartFile objects into DTOs.
     *
     * This is important because MultipartFile
     * cannot safely be passed into @Async methods.
     */
    private List<EmailAttachment> buildAttachments(
            MultipartFile[] attachments)
            throws IOException {

        List<EmailAttachment> emailAttachments =
                new ArrayList<>();

        if (attachments == null) {
            return emailAttachments;
        }

        for (MultipartFile file : attachments) {

            if (file.isEmpty()) {
                continue;
            }

            log.debug(
                    "Received attachment. Name={}, Size={}",
                    file.getOriginalFilename(),
                    file.getSize()
            );

            emailAttachments.add(
                    new EmailAttachment(
                            file.getOriginalFilename(),
                            file.getBytes(),
                            file.getSize(),
                            file.getContentType()
                    )
            );
        }

        return emailAttachments;
    }
}