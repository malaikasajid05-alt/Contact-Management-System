package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.entity.User;
import com.malaika.backend.enums.DetailType;
import com.malaika.backend.exception.ContactDetailNotFoundException;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.mapper.ContactDetailMapper;
import com.malaika.backend.repository.ContactDetailRepository;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.impl.ContactDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactDetailServiceImplTest {

    @Mock ContactDetailRepository detailRepository;
    @Mock ContactRepository contactRepository;
    @Mock CurrentUser currentUser;
    @Mock ContactDetailMapper mapper;

    @InjectMocks
    ContactDetailServiceImpl service;

    private User ownerUser;
    private User otherUser;
    private Contact contact;

    @BeforeEach
    void setUp() {
        ownerUser = new User();
        ownerUser.setId(1L);

        otherUser = new User();
        otherUser.setId(2L);

        contact = new Contact();
        contact.setId(1L);
        contact.setUser(ownerUser);
    }

    @Test
    void addDetail_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");
        dto.setValue("+1234567890");
        dto.setLabel("Mobile");
        dto.setContactId(1L);

        ContactDetail savedDetail = new ContactDetail();
        savedDetail.setId(1L);
        savedDetail.setContact(contact);
        savedDetail.setValue(dto.getValue());
        savedDetail.setLabel(dto.getLabel());
        savedDetail.setType(DetailType.PHONE);

        ContactDetailDto expectedDto = new ContactDetailDto();
        expectedDto.setId(1L);
        expectedDto.setType("PHONE");
        expectedDto.setValue("+1234567890");
        expectedDto.setLabel("Mobile");
        expectedDto.setContactId(1L);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(detailRepository.save(any(ContactDetail.class))).thenReturn(savedDetail);
        when(mapper.toDto(any(ContactDetail.class))).thenReturn(expectedDto);

        ContactDetailDto result = service.addDetail(1L, dto);

        assertNotNull(result);
        assertEquals("PHONE", result.getType());
        assertEquals("+1234567890", result.getValue());
        assertEquals("Mobile", result.getLabel());

        verify(contactRepository).findById(1L);
        verify(detailRepository).save(argThat(detail ->
                detail.getValue().equals("+1234567890") &&
                        detail.getLabel().equals("Mobile") &&
                        detail.getType() == DetailType.PHONE
        ));
        verify(mapper).toDto(savedDetail);
        verify(mapper, never()).toEntity(any());
    }

    @Test
    void addDetail_withEmailType_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("EMAIL");
        dto.setValue("test@example.com");
        dto.setLabel("Work");

        ContactDetail savedDetail = new ContactDetail();
        savedDetail.setType(DetailType.EMAIL);

        ContactDetailDto resultDto = new ContactDetailDto();
        resultDto.setType("EMAIL");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(detailRepository.save(any())).thenReturn(savedDetail);
        when(mapper.toDto(savedDetail)).thenReturn(resultDto);

        ContactDetailDto result = service.addDetail(1L, dto);

        assertNotNull(result);
        assertEquals("EMAIL", result.getType());
    }

    @Test
    void addDetail_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");
        dto.setValue("+1234567890");

        assertThrows(ContactNotFoundException.class, () -> service.addDetail(99L, dto));
        verify(detailRepository, never()).save(any());
    }

    @Test
    void addDetail_unauthorizedUser_throwsRuntimeException() {
        when(currentUser.getCurrentUserId()).thenReturn(2L); // Different user
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");
        dto.setValue("+1234567890");

        assertThrows(RuntimeException.class, () -> service.addDetail(1L, dto));
        verify(detailRepository, never()).save(any());
    }

    @Test
    void addDetail_invalidDetailType_throwsIllegalArgumentException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("INVALID_TYPE");
        dto.setValue("+1234567890");

        assertThrows(IllegalArgumentException.class, () -> service.addDetail(1L, dto));
        verify(detailRepository, never()).save(any());
    }

    @Test
    void getDetails_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetail detail1 = new ContactDetail();
        detail1.setId(1L);
        detail1.setType(DetailType.PHONE);
        detail1.setValue("+1234567890");

        ContactDetail detail2 = new ContactDetail();
        detail2.setId(2L);
        detail2.setType(DetailType.EMAIL);
        detail2.setValue("test@example.com");

        ContactDetailDto dto1 = new ContactDetailDto();
        dto1.setId(1L);
        dto1.setType("PHONE");

        ContactDetailDto dto2 = new ContactDetailDto();
        dto2.setId(2L);
        dto2.setType("EMAIL");

        when(detailRepository.findByContactId(1L)).thenReturn(List.of(detail1, detail2));
        when(mapper.toDto(detail1)).thenReturn(dto1);
        when(mapper.toDto(detail2)).thenReturn(dto2);

        List<ContactDetailDto> result = service.getDetails(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PHONE", result.get(0).getType());
        assertEquals("EMAIL", result.get(1).getType());

        verify(contactRepository).findById(1L);
        verify(detailRepository).findByContactId(1L);
        verify(mapper, times(2)).toDto(any(ContactDetail.class));
    }

    @Test
    void getDetails_emptyList_returnsEmpty() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(detailRepository.findByContactId(1L)).thenReturn(List.of());

        List<ContactDetailDto> result = service.getDetails(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getDetails_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> service.getDetails(99L));
        verify(detailRepository, never()).findByContactId(any());
    }

    @Test
    void getDetails_unauthorizedUser_throwsRuntimeException() {
        when(currentUser.getCurrentUserId()).thenReturn(2L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        assertThrows(RuntimeException.class, () -> service.getDetails(1L));
        verify(detailRepository, never()).findByContactId(any());
    }

    @Test
    void updateDetail_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetail existingDetail = new ContactDetail();
        existingDetail.setId(1L);
        existingDetail.setContact(contact);
        existingDetail.setType(DetailType.PHONE);
        existingDetail.setValue("+1234567890");

        when(detailRepository.findByIdAndContactId(1L, 1L)).thenReturn(Optional.of(existingDetail));
        when(detailRepository.save(existingDetail)).thenReturn(existingDetail);

        ContactDetailDto updateDto = new ContactDetailDto();
        updateDto.setType("EMAIL");
        updateDto.setValue("updated@example.com");
        updateDto.setLabel("Updated");

        ContactDetailDto resultDto = new ContactDetailDto();
        resultDto.setType("EMAIL");
        resultDto.setValue("updated@example.com");

        when(mapper.toDto(existingDetail)).thenReturn(resultDto);

        ContactDetailDto result = service.updateDetail(1L, 1L, updateDto);

        assertNotNull(result);
        assertEquals("EMAIL", result.getType());
        assertEquals("updated@example.com", existingDetail.getValue());
        assertEquals("Updated", existingDetail.getLabel());
        assertEquals(DetailType.EMAIL, existingDetail.getType());
        verify(detailRepository).save(existingDetail);
    }

    @Test
    void updateDetail_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");

        assertThrows(ContactNotFoundException.class,
                () -> service.updateDetail(99L, 1L, dto));

        verify(detailRepository, never()).save(any());
    }

    @Test
    void updateDetail_unauthorizedUser_throwsRuntimeException() {
        when(currentUser.getCurrentUserId()).thenReturn(2L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");

        assertThrows(RuntimeException.class,
                () -> service.updateDetail(1L, 1L, dto));

        verify(detailRepository, never()).save(any());
    }

    @Test
    void updateDetail_detailNotFound_throwsContactDetailNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(detailRepository.findByIdAndContactId(99L, 1L)).thenReturn(Optional.empty());

        ContactDetailDto dto = new ContactDetailDto();
        dto.setType("PHONE");

        assertThrows(ContactDetailNotFoundException.class,
                () -> service.updateDetail(1L, 99L, dto));

        verify(detailRepository, never()).save(any());
    }

    // ==================== deleteDetail ====================

    @Test
    void deleteDetail_success() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactDetail detail = new ContactDetail();
        detail.setId(1L);
        when(detailRepository.findByIdAndContactId(1L, 1L)).thenReturn(Optional.of(detail));

        assertDoesNotThrow(() -> service.deleteDetail(1L, 1L));
        verify(detailRepository).delete(detail);
    }

    @Test
    void deleteDetail_contactNotFound_throwsContactNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class,
                () -> service.deleteDetail(99L, 1L));

        verify(detailRepository, never()).delete(any());
    }

    @Test
    void deleteDetail_unauthorizedUser_throwsRuntimeException() {
        when(currentUser.getCurrentUserId()).thenReturn(2L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        assertThrows(RuntimeException.class,
                () -> service.deleteDetail(1L, 1L));

        verify(detailRepository, never()).delete(any());
    }

    @Test
    void deleteDetail_detailNotFound_throwsContactDetailNotFoundException() {
        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(detailRepository.findByIdAndContactId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ContactDetailNotFoundException.class,
                () -> service.deleteDetail(1L, 99L));

        verify(detailRepository, never()).delete(any());
    }
}