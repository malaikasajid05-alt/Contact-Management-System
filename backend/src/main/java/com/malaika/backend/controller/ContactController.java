package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/{id}")
    public ContactDto updateContact(@PathVariable Long id, @RequestBody ContactDto contactDto) {
        return contactService.updateContact(id, contactDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long id) {

        contactService.deleteContact(id);
    }

    @GetMapping("/search")
    public List<ContactDto> search(@RequestParam String q) {

        return contactService.searchContacts(q);
    }

    @GetMapping
    public Page<ContactDto> getMyContacts(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return contactService.getMyContacts(PageRequest.of(page, size));
    }
}