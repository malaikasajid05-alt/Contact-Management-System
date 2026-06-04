package com.malaika.backend.service.impl;

import com.malaika.backend.dto.EmailDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.Email;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.exception.EmailNotFoundException;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.EmailMapper;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.EmailRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final ContactRepository contactRepository;
    private final EmailMapper emailMapper;
    private final CurrentUser currentUser;

    private void validateContactOwnership(Contact contact, Long userId) {
        if (!contact.getUser().getId().equals(userId)) {
            log.warn("Unauthorized access attempt for contactId: {}", contact.getId());
            throw new RuntimeException("Not allowed to access this contact");
        }
    }

    @Override
    public EmailDto addEmail(Long contactId, EmailDto dto) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Adding email to contactId: {} for userId: {}", contactId, userId);

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> {
                    log.error("Contact not found: {}", contactId);
                    return new ContactNotFoundException("Contact not found with id: " + contactId);
                });

        validateContactOwnership(contact, userId);

        Email email = emailMapper.toEntity(dto);
        email.setContact(contact);

        Email saved = emailRepository.save(email);

        log.info("Email created with id: {}", saved.getId());

        return emailMapper.toDto(saved);
    }

    @Override
    public List<EmailDto> getEmailsByContact(Long contactId) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Fetching emails for contactId: {}", contactId);

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        validateContactOwnership(contact, userId);

        List<Email> emails = emailRepository.findByContactId(contactId);

        return emails.stream()
                .map(emailMapper::toDto)
                .toList();
    }

    @Override
    public EmailDto updateEmail(Long contactId, Long emailId, EmailDto dto) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Updating emailId: {} for contactId: {}", emailId, contactId);

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        validateContactOwnership(contact, userId);

        Email email = emailRepository.findByIdAndContactId(emailId, contactId)
                .orElseThrow(() -> {
                    log.error("Email not found: {}", emailId);
                    return new EmailNotFoundException("Email not found with id: " + emailId);
                });

        email.setEmail(dto.getEmail());
        email.setELabel(dto.getELabel());

        Email saved = emailRepository.save(email);

        log.info("Email updated successfully: {}", emailId);

        return emailMapper.toDto(saved);
    }

    @Override
    public void deleteEmail(Long contactId, Long emailId) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Deleting emailId: {} for contactId: {}", emailId, contactId);

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));

        validateContactOwnership(contact, userId);

        Email email = emailRepository.findByIdAndContactId(emailId, contactId)
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        emailRepository.delete(email);

        log.info("Email deleted successfully: {}", emailId);
    }
}