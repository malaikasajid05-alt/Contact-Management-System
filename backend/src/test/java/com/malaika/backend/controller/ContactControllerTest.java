package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private ContactDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new ContactDto();
    }

    @Test
    void createContact_delegatesToServiceAndReturnsResult() {
        when(contactService.createContact(sampleDto)).thenReturn(sampleDto);

        ContactDto result = contactController.createContact(sampleDto);

        assertEquals(sampleDto, result);
        verify(contactService).createContact(sampleDto);
    }

    @Test
    void getContactById_delegatesToServiceAndReturnsResult() {
        when(contactService.getContactById(1L)).thenReturn(sampleDto);

        ContactDto result = contactController.getContactById(1L);

        assertEquals(sampleDto, result);
        verify(contactService).getContactById(1L);
    }

    @Test
    void updateContact_delegatesToServiceAndReturnsResult() {
        when(contactService.updateContact(1L, sampleDto)).thenReturn(sampleDto);

        ContactDto result = contactController.updateContact(1L, sampleDto);

        assertEquals(sampleDto, result);
        verify(contactService).updateContact(1L, sampleDto);
    }

    @Test
    void deleteContact_delegatesToService() {
        doNothing().when(contactService).deleteContact(1L);

        contactController.deleteContact(1L);

        verify(contactService).deleteContact(1L);
    }

    @Test
    void search_delegatesToServiceAndReturnsList() {
        List<ContactDto> contacts = List.of(sampleDto);
        when(contactService.searchContacts("john")).thenReturn(contacts);

        List<ContactDto> result = contactController.search("john");

        assertEquals(1, result.size());
        assertEquals(sampleDto, result.get(0));
        verify(contactService).searchContacts("john");
    }

    @Test
    void search_emptyQuery_returnsEmptyList() {
        when(contactService.searchContacts("")).thenReturn(List.of());

        List<ContactDto> result = contactController.search("");

        assertTrue(result.isEmpty());
    }

    @Test
    void getMyContacts_delegatesToServiceWithPageRequest() {
        Page<ContactDto> page = new PageImpl<>(List.of(sampleDto));
        when(contactService.getMyContacts(PageRequest.of(0, 10))).thenReturn(page);

        Page<ContactDto> result = contactController.getMyContacts(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(contactService).getMyContacts(PageRequest.of(0, 10));
    }
}