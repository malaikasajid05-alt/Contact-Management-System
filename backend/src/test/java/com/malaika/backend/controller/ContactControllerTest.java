package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @Test
    void createContact_success() throws Exception {

        when(contactService.createContact(any()))
                .thenReturn(new ContactDto());

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName":"John",
                                  "lastName":"Doe",
                                  "title":"Friend"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void getAllContacts_success() throws Exception {

        when(contactService.getMyContacts())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_success() throws Exception {

        when(contactService.getContactById(1L))
                .thenReturn(new ContactDto());

        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_success() throws Exception {

        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isOk());
    }
}