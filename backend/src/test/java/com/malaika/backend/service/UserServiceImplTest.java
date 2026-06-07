package com.malaika.backend.service;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.UserProfileMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileMapper mapper;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getProfile_success() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ProfileResponseDto dto = new ProfileResponseDto();

        when(mapper.toDto(user)).thenReturn(dto);

        ProfileResponseDto result = userService.getProfile();

        assertNotNull(result);

        verify(userRepository).findById(1L);
        verify(mapper).toDto(user);
    }

    @Test
    void getProfile_userNotFound_throwsUserNotFoundException() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getProfile());

        verify(mapper, never()).toDto(any());
    }

    @Test
    void changePassword_success() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("oldPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("newPassword");

        when(passwordEncoder.matches("oldPassword", "encodedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedNewPassword");

        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(dto));

        assertEquals("encodedNewPassword", user.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    void changePassword_userNotFound_throwsUserNotFoundException() {

        when(currentUser.getCurrentUserId()).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("oldPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("newPassword");

        assertThrows(UserNotFoundException.class,
                () -> userService.changePassword(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_wrongCurrentPassword_throwsException() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedPassword");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("wrongPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("newPassword");

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_confirmMismatch_throwsException() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("oldPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("differentPassword");

        when(passwordEncoder.matches("oldPassword", "encodedOldPassword"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_samePassword_throwsException() {

        when(currentUser.getCurrentUserId()).thenReturn(1L);

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("oldPassword");
        dto.setNewPassword("oldPassword");
        dto.setConfirmPassword("oldPassword");

        when(passwordEncoder.matches("oldPassword", "encodedOldPassword"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(dto));

        verify(userRepository, never()).save(any());
    }
}