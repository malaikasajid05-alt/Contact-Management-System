package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.ContactMapper;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = contactMapper.toEntity(contactDto);
        contact.setUser(user);

        Contact saved = contactRepository.save(contact);

        return contactMapper.toDto(saved);
    }

    @Override
    public ContactDto getContactById(Long id) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        return contactMapper.toDto(contact);
    }

    @Override
    public List<ContactDto> getMyContacts() {

        Long userId = currentUser.getCurrentUserId();

        List<Contact> contacts = contactRepository.findByUserId(userId);

        return contacts.stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    public ContactDto updateContact(Long id, ContactDto contactDto) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setTitle(contactDto.getTitle());

        Contact saved = contactRepository.save(contact);

        return contactMapper.toDto(saved);
    }

    @Override
    public void deleteContact(Long id) {

        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Contact not found or not allowed"));

        contactRepository.delete(contact);
    }
}