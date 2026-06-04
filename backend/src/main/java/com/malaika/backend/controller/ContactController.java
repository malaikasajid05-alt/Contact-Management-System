package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ContactDto createContact(@RequestBody ContactDto contactDto) {
        return contactService.createContact(contactDto);
    }

    @GetMapping("/{id}")
    public ContactDto getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @GetMapping
    public List<ContactDto> getMyContacts() {
        return contactService.getMyContacts();
    }

    @PutMapping("/{id}")
    public ContactDto updateContact(
            @PathVariable Long id,
            @RequestBody ContactDto contactDto) {
        return contactService.updateContact(id, contactDto);
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "Contact deleted successfully";
    }
}