package com.malaika.backend.service;

import com.malaika.backend.dto.ContactDetailDto;

import java.util.List;

public interface ContactDetailService {

    ContactDetailDto addDetail(Long contactId, ContactDetailDto dto);

    List<ContactDetailDto> getDetails(Long contactId);

    ContactDetailDto updateDetail(Long contactId, Long detailId, ContactDetailDto dto);

    void deleteDetail(Long contactId, Long detailId);
}