package com.malaika.backend.controller;

import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.security.JwtAuthenticationFilter;
import com.malaika.backend.security.JwtService;
import com.malaika.backend.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void getProfile_success() throws Exception {

        when(userService.getProfile())
                .thenReturn(new ProfileResponseDto());

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void changePassword_success() throws Exception {

        doNothing().when(userService).changePassword(any());

        mockMvc.perform(put("/api/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "oldPassword": "currentPass123",
                                  "newPassword": "newSecurePass456"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
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