package com.malaika.backend.controller;

import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.security.JwtAuthenticationFilter;
import com.malaika.backend.security.JwtService;
import com.malaika.backend.service.AuthService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthControllerTest.TestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void register_success() throws Exception {

        when(authService.register(any()))
                .thenReturn(new AuthResponseDto());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"test@test.com",
                                  "password":"123"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void login_success() throws Exception {

        when(authService.login(any()))
                .thenReturn(new AuthResponseDto());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email":"test@test.com",
                                  "password":"123"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return Mockito.mock(JwtAuthenticationFilter.class);
        }
    }
}