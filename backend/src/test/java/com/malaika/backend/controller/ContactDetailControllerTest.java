package com.malaika.backend.controller;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.security.JwtAuthenticationFilter;
import com.malaika.backend.security.JwtService;
import com.malaika.backend.service.ContactDetailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactDetailController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ContactDetailControllerTest.TestConfig.class)
class ContactDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactDetailService contactDetailService;

    @Test
    void addDetail_success() throws Exception {

        when(contactDetailService.addDetail(eq(1L), any()))
                .thenReturn(new ContactDetailDto());

        mockMvc.perform(post("/api/contacts/1/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "test@gmail.com",
                                  "type":  "EMAIL"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void getDetails_success() throws Exception {

        when(contactDetailService.getDetails(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/contacts/1/details"))
                .andExpect(status().isOk());
    }

    @Test
    void updateDetail_success() throws Exception {

        when(contactDetailService.updateDetail(eq(1L), eq(2L), any()))
                .thenReturn(new ContactDetailDto());

        mockMvc.perform(put("/api/contacts/1/details/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "updated@gmail.com",
                                  "type":  "PHONE"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDetail_success() throws Exception {

        doNothing().when(contactDetailService).deleteDetail(1L, 2L);

        mockMvc.perform(delete("/api/contacts/1/details/2"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ContactDetailService contactDetailService() {
            return Mockito.mock(ContactDetailService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
            return Mockito.mock(JwtAuthenticationFilter.class);
        }
    }
}