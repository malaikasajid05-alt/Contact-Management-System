package com.malaika.backend.service.impl;

import com.malaika.backend.dto.EmailDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.Email;
import com.malaika.backend.mapper.EmailMapper;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.EmailRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final ContactRepository contactRepository;
    private final EmailMapper emailMapper;
    private final CurrentUser currentUser;

    private void validateContactOwnership(Contact contact, Long userId) {
        if (!contact.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not allowed");
        }
    }

    @Override
    public EmailDto addEmail(Long contactId, EmailDto dto) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        validateContactOwnership(contact, userId);

        Email email = emailMapper.toEntity(dto);
        email.setContact(contact);

        Email saved = emailRepository.save(email);

        return emailMapper.toDto(saved);
    }

    @Override
    public List<EmailDto> getEmailsByContact(Long contactId) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        validateContactOwnership(contact, userId);

        List<Email> emails = emailRepository.findByContactId(contactId);

        return emails.stream()
                .map(emailMapper::toDto)
                .toList();
    }

    @Override
    public EmailDto updateEmail(Long contactId, Long emailId, EmailDto dto) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        validateContactOwnership(contact, userId);

        Email email = emailRepository.findByIdAndContactId(emailId, contactId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        email.setEmail(dto.getEmail());
        email.setELabel(dto.getELabel());

        Email saved = emailRepository.save(email);

        return emailMapper.toDto(saved);
    }

    @Override
    public void deleteEmail(Long contactId, Long emailId) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        validateContactOwnership(contact, userId);

        Email email = emailRepository.findByIdAndContactId(emailId, contactId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        emailRepository.delete(email);
    }
}