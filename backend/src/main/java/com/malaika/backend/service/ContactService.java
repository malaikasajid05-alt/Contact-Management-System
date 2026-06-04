package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDto;

import java.util.List;

public interface ContactService {

    ContactDto createContact(ContactDto contactDto);

    ContactDto getContactById(Long id);

    List<ContactDto> getMyContacts();

    ContactDto updateContact(Long id, ContactDto contactDto);

    void deleteContact(Long id);
}