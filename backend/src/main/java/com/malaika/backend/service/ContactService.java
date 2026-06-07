package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    List<ContactDto> searchContacts(String query);

    ContactDto createContact(ContactDto contactDto);

    ContactDto getContactById(Long id);

    Page<ContactDto> getMyContacts(Pageable pageable);

    ContactDto updateContact(Long id, ContactDto contactDto);

    void deleteContact(Long id);
}