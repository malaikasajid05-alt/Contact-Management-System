package com.malaika.backend.service;

import com.malaika.backend.dto.EmailDto;

import java.util.List;

public interface EmailService {

    EmailDto addEmail(Long contactId, EmailDto dto);

    List<EmailDto> getEmailsByContact(Long contactId);

    EmailDto updateEmail(Long contactId, Long emailId, EmailDto dto);

    void deleteEmail(Long contactId, Long emailId);
}