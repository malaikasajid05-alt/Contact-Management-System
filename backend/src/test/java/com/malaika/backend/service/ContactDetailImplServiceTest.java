package com.malaika.backend.service;

import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.mapper.ContactDetailMapper;
import com.malaika.backend.repository.ContactDetailRepository;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.impl.ContactDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContactDetailServiceImplTest {

    @Mock ContactDetailRepository detailRepository;
    @Mock ContactRepository contactRepository;
    @Mock CurrentUser currentUser;
    @Mock ContactDetailMapper mapper;

    @InjectMocks
    ContactDetailServiceImpl service;

    @Test
    void addDetail_success() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(new Contact()));
        when(detailRepository.save(any())).thenReturn(new ContactDetail());
        when(mapper.toEntity(any())).thenReturn(new ContactDetail());
        when(mapper.toDto(any())).thenReturn(new com.malaika.backend.dto.ContactDetailDto());

        assertNotNull(service.addDetail(1L, new com.malaika.backend.dto.ContactDetailDto()));
    }

    @Test
    void getDetails_success() {

        when(detailRepository.findByContactId(1L))
                .thenReturn(List.of(new ContactDetail()));

        when(mapper.toDto(any())).thenReturn(new com.malaika.backend.dto.ContactDetailDto());

        assertNotNull(service.getDetails(1L));
    }
}