package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.exception.UserNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void createContact_withDetails_setsContactOnEachDetail() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Contact contactEntity = new Contact();
        ContactDetail detail1 = new ContactDetail();
        ContactDetail detail2 = new ContactDetail();
        List<ContactDetail> details = new ArrayList<>(List.of(detail1, detail2));
        contactEntity.setDetails(details);

        when(contactMapper.toEntity(any())).thenReturn(contactEntity);

        Contact saved = new Contact();
        when(contactRepository.save(any())).thenReturn(saved);
        when(contactMapper.toDto(saved)).thenReturn(new ContactDto());

        ContactDto result = contactService.createContact(new ContactDto());

        assertNotNull(result);
        assertEquals(contactEntity, detail1.getContact());
        assertEquals(contactEntity, detail2.getContact());
    }

    @Test
    void createContact_withEmptyDetails_doesNotThrow() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        Contact contactEntity = new Contact();
        contactEntity.setDetails(new ArrayList<>());
        when(contactMapper.toEntity(any())).thenReturn(contactEntity);

        Contact saved = new Contact();
        when(contactRepository.save(any())).thenReturn(saved);
        when(contactMapper.toDto(saved)).thenReturn(new ContactDto());

        assertDoesNotThrow(() -> contactService.createContact(new ContactDto()));
    }

    @Test
    void createContact_userNotFound_throwsUserNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ContactDto dto = new ContactDto();
        assertThrows(UserNotFoundException.class,
                () -> contactService.createContact(dto));

        verify(contactRepository, never()).save(any());
    }

    @Test
    void getContactById_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Contact contact = new Contact();
        contact.setId(1L);
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(contact));

        ContactDto expected = new ContactDto();
        when(contactMapper.toDto(contact)).thenReturn(expected);

        ContactDto result = contactService.getContactById(1L);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(contactRepository).findByIdAndUserId(1L, 1L);
    }

    @Test
    void getContactById_notFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class,
                () -> contactService.getContactById(99L));
    }

    @Test
    void getMyContacts_returnsPaginatedContacts() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Contact contact = new Contact();
        ContactDto dto = new ContactDto();

        Page<Contact> contactPage = new PageImpl<>(List.of(contact));
        Pageable pageable = PageRequest.of(0, 10);

        when(contactRepository.findByUserId(1L, pageable)).thenReturn(contactPage);
        when(contactMapper.toDto(contact)).thenReturn(dto);

        Page<ContactDto> result = contactService.getMyContacts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contactRepository).findByUserId(1L, pageable);
    }

    @Test
    void getMyContacts_emptyPage_returnsEmpty() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Page<Contact> emptyPage = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);
        when(contactRepository.findByUserId(1L, pageable)).thenReturn(emptyPage);

        Page<ContactDto> result = contactService.getMyContacts(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void updateContact_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Contact existing = new Contact();
        existing.setId(1L);
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(existing));
        when(contactRepository.save(existing)).thenReturn(existing);

        ContactDto updateDto = new ContactDto();
        updateDto.setFirstName("John");
        updateDto.setLastName("Doe");
        updateDto.setTitle("Mr");

        ContactDto expectedResult = new ContactDto();
        when(contactMapper.toDto(existing)).thenReturn(expectedResult);

        ContactDto result = contactService.updateContact(1L, updateDto);

        assertNotNull(result);
        assertEquals("John", existing.getFirstName());
        assertEquals("Doe", existing.getLastName());
        assertEquals("Mr", existing.getTitle());
        verify(contactRepository).save(existing);
    }

    @Test
    void updateContact_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        ContactDto dto = new ContactDto();
        assertThrows(ContactNotFoundException.class,
                () -> contactService.updateContact(99L, dto));

        verify(contactRepository, never()).save(any());
    }

    @Test
    void deleteContact_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Contact contact = new Contact();
        contact.setId(1L);
        when(contactRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(contact));

        assertDoesNotThrow(() -> contactService.deleteContact(1L));

        verify(contactRepository).delete(contact);
    }

    @Test
    void deleteContact_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class,
                () -> contactService.deleteContact(99L));

        verify(contactRepository, never()).delete(any());
    }

    @Test
    void searchContacts_returnsMatchingContacts() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        Contact contact = new Contact();
        contact.setFirstName("John");
        ContactDto dto = new ContactDto();
        dto.setFirstName("John");

        when(contactRepository.searchByUserId(1L, "John")).thenReturn(List.of(contact));
        when(contactMapper.toDto(contact)).thenReturn(dto);

        List<ContactDto> result = contactService.searchContacts("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(contactRepository).searchByUserId(1L, "John");
    }

    @Test
    void searchContacts_noResults_returnsEmptyList() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.searchByUserId(1L, "xyz")).thenReturn(List.of());

        List<ContactDto> result = contactService.searchContacts("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}