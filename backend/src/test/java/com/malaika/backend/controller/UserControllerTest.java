package com.malaika.backend.controller;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
                                  "oldPassword":"123",
                                  "newPassword":"456"
                                }
                                """))
                .andExpect(status().isOk());
    }
}