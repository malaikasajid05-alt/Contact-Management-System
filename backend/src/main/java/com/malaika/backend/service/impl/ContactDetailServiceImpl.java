package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.enums.DetailType;
import com.malaika.backend.exception.ContactDetailNotFoundException;
import com.malaika.backend.exception.ContactNotFoundException;
import com.malaika.backend.mapper.ContactDetailMapper;
import com.malaika.backend.repository.ContactDetailRepository;
import com.malaika.backend.repository.ContactRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.ContactDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactDetailServiceImpl implements ContactDetailService {

    private static final String CONTACT_NOT_FOUND = "Contact not found with id: ";

    private final ContactDetailRepository detailRepository;
    private final ContactRepository contactRepository;
    private final ContactDetailMapper mapper;
    private final CurrentUser currentUser;

    private void validate(Contact contact, Long userId) {
        if (!contact.getUser().getId().equals(userId))
            throw new ContactNotFoundException("Not allowed to access this contact");
    }

    @Override
    public ContactDetailDto addDetail(Long contactId, ContactDetailDto dto) {
        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND + contactId));

        validate(contact, userId);

        ContactDetail detail = new ContactDetail();
        detail.setContact(contact);
        detail.setValue(dto.getValue());
        detail.setLabel(dto.getLabel());
        detail.setType(DetailType.valueOf(dto.getType()));

        return mapper.toDto(detailRepository.save(detail));
    }

    @Override
    public List<ContactDetailDto> getDetails(Long contactId) {
        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND + contactId));

        validate(contact, userId);

        return detailRepository.findByContactId(contactId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ContactDetailDto updateDetail(Long contactId, Long detailId, ContactDetailDto dto) {
        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND + contactId));

        validate(contact, userId);

        ContactDetail detail = detailRepository.findByIdAndContactId(detailId, contactId)
                .orElseThrow(() ->
                        new ContactDetailNotFoundException(
                                "Detail not found with id: " + detailId + " for contact: " + contactId));

        detail.setValue(dto.getValue());
        detail.setLabel(dto.getLabel());
        detail.setType(DetailType.valueOf(dto.getType()));

        return mapper.toDto(detailRepository.save(detail));
    }

    @Override
    public void deleteDetail(Long contactId, Long detailId) {
        Long userId = currentUser.getCurrentUserId();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND + contactId));

        validate(contact, userId);

        ContactDetail detail = detailRepository.findByIdAndContactId(detailId, contactId)
                .orElseThrow(() ->
                        new ContactDetailNotFoundException(
                                "Detail not found with id: " + detailId));

        detailRepository.delete(detail);
    }
}