package com.malaika.backend.controller;

import com.malaika.backend.dto.EmailDto;
import com.malaika.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts/{contactId}/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public EmailDto addEmail(
            @PathVariable Long contactId,
            @RequestBody EmailDto dto) {

        return emailService.addEmail(contactId, dto);
    }

    @GetMapping
    public List<EmailDto> getEmailsByContact(
            @PathVariable Long contactId) {

        return emailService.getEmailsByContact(contactId);
    }

    @PutMapping("/{emailId}")
    public EmailDto updateEmail(
            @PathVariable Long contactId,
            @PathVariable Long emailId,
            @RequestBody EmailDto dto) {

        return emailService.updateEmail(contactId, emailId, dto);
    }

    @DeleteMapping("/{emailId}")
    public String deleteEmail(
            @PathVariable Long contactId,
            @PathVariable Long emailId) {

        emailService.deleteEmail(contactId, emailId);
        return "Email deleted successfully";
    }
}