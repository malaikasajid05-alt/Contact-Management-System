package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.ContactService;
import com.malaika.backend.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;
    private final CurrentUser currentUser;

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
                    return new ContactNotFoundException("Contact not found with id: " + id);
                });

        return contactMapper.toDto(contact);
    }

    @Override
    public List<ContactDto> getMyContacts() {

        Long userId = currentUser.getCurrentUserId();

        log.info("Fetching all contacts for userId: {}", userId);

        List<Contact> contacts = contactRepository.findByUserId(userId);

        return contacts.stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    public ContactDto updateContact(Long id, ContactDto contactDto) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Updating contact {} for userId: {}", id, userId);

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.error("Contact not found for update: {} userId: {}", id, userId);
                    return new ContactNotFoundException("Contact not found with id: " + id);
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
                    return new ContactNotFoundException("Contact not found with id: " + id);
                });

        contactRepository.delete(contact);

        log.info("Contact deleted successfully: {}", id);
    }
}