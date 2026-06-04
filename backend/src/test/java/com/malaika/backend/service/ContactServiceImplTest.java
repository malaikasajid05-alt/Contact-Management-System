package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.ContactMapper;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.impl.ContactServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock ContactRepository contactRepository;
    @Mock UserRepository userRepository;
    @Mock ContactMapper contactMapper;
    @Mock CurrentUser currentUser;

    @InjectMocks
    ContactServiceImpl contactService;

    @Test
    void createContact_success() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(contactRepository.save(any())).thenReturn(new Contact());
        when(contactMapper.toEntity(any())).thenReturn(new Contact());
        when(contactMapper.toDto(any())).thenReturn(new ContactDto());

        ContactDto result = contactService.createContact(new ContactDto());

        assertNotNull(result);
        verify(contactRepository).save(any());
    }
}