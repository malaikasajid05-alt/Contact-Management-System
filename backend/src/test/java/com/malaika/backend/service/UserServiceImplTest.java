package com.malaika.backend.service;

import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.UserProfileMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock UserProfileMapper mapper;
    @Mock CurrentUser currentUser;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getProfile_success() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        userService.getProfile();

        verify(userRepository).findById(1L);
    }

    @Test
    void getProfile_userNotFound() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getProfile());
    }
}