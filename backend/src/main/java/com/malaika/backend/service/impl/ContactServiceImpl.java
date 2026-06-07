package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.ContactMapper;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private static final String CONTACT_NOT_FOUND = "Contact not found with id: ";

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private final CurrentUser currentUser;

    @Override
    public List<ContactDto> searchContacts(String query) {
        Long userId = currentUser.getCurrentUserId();
        List<Contact> contacts = contactRepository.searchByUserId(userId, query);
        return contacts.stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    public ContactDto createContact(ContactDto contactDto) {
        Long userId = currentUser.getCurrentUserId();
        log.info("Creating contact for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        Contact contact = contactMapper.toEntity(contactDto);
        contact.setUser(user);

        if (contact.getDetails() != null && !contact.getDetails().isEmpty())
            contact.getDetails().forEach(detail -> detail.setContact(contact));

        Contact saved = contactRepository.save(contact);
        log.info("Contact created with id: {}", saved.getId());
        return contactMapper.toDto(saved);
    }

    @Override
    public ContactDto getContactById(Long id) {
        Long userId = currentUser.getCurrentUserId();
        log.info("Fetching contact {} for userId: {}", id, userId);

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.error("Contact not found: {} for userId: {}", id, userId);
                    return new ContactNotFoundException(CONTACT_NOT_FOUND + id);
                });

        return contactMapper.toDto(contact);
    }

    @Override
    public Page<ContactDto> getMyContacts(Pageable pageable) {
        Long userId = currentUser.getCurrentUserId();
        log.info("Fetching contacts for userId: {}", userId);
        return contactRepository.findByUserId(userId, pageable)
                .map(contactMapper::toDto);
    }

    @Override
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        Long userId = currentUser.getCurrentUserId();
        log.info("Updating contact {} for userId: {}", id, userId);

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.error("Contact not found for update: {} userId: {}", id, userId);
                    return new ContactNotFoundException(CONTACT_NOT_FOUND + id);
                });

        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setTitle(contactDto.getTitle());

        Contact saved = contactRepository.save(contact);
        log.info("Contact updated successfully: {}", id);
        return contactMapper.toDto(saved);
    }

    @Override
    public void deleteContact(Long id) {
        Long userId = currentUser.getCurrentUserId();
        log.info("Deleting contact {} for userId: {}", id, userId);

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.error("Contact not found for delete: {} userId: {}", id, userId);
                    return new ContactNotFoundException(CONTACT_NOT_FOUND + id);
                });

        contactRepository.delete(contact);
        log.info("Contact deleted successfully: {}", id);
    }
}