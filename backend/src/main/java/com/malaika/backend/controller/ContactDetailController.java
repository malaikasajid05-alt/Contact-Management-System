package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.service.ContactDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts/{contactId}/details")
@RequiredArgsConstructor
public class ContactDetailController {

    private final ContactDetailService service;

    @PostMapping
    public ContactDetailDto add(@PathVariable Long contactId,
                                @RequestBody ContactDetailDto dto) {
        return service.addDetail(contactId, dto);
    }

    @GetMapping
    public List<ContactDetailDto> get(@PathVariable Long contactId) {
        return service.getDetails(contactId);
    }

    @PutMapping("/{detailId}")
    public ContactDetailDto update(@PathVariable Long contactId,
                                   @PathVariable Long detailId,
                                   @RequestBody ContactDetailDto dto) {
        return service.updateDetail(contactId, detailId, dto);
    }

    @DeleteMapping("/{detailId}")
    public void delete(@PathVariable Long contactId,
                       @PathVariable Long detailId) {
        service.deleteDetail(contactId, detailId);
    }
}