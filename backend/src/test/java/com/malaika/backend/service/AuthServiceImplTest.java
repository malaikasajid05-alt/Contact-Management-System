package com.malaika.backend.service;

import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.dto.LoginRequestDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.AuthMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.JwtService;
import com.malaika.backend.service.impl.AuthImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock AuthMapper authMapper;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;

    @InjectMocks
    AuthImpl authService;

    @Test
    void register_success() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123");

        User user = new User();
        user.setId(1L);

        AuthResponseDto responseDto = new AuthResponseDto();

        when(authMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(anyLong())).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.register(dto);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(1L);
        assertEquals("token", result.getToken());
    }

    @Test
    void register_emailAlreadyExists_throwsIllegalArgumentException() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("existing@test.com");
        dto.setPassword("123");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register(dto));

        assertEquals("Email already registered", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_phoneAlreadyExists_throwsIllegalArgumentException() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("new@test.com");
        dto.setPnumber("+123456789");
        dto.setPassword("123");

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.existsByPnumber("+123456789")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.register(dto));

        assertEquals("Phone Number already registered", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_nullEmail_skipsEmailCheck() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail(null);
        dto.setPnumber("+123456789");
        dto.setPassword("123");

        User user = new User();
        user.setId(1L);
        AuthResponseDto responseDto = new AuthResponseDto();

        when(userRepository.existsByPnumber("+123456789")).thenReturn(false);
        when(authMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(1L)).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.register(dto);

        assertNotNull(result);
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    void register_nullPhone_skipsPhoneCheck() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@test.com");
        dto.setPnumber(null);
        dto.setPassword("123");

        User user = new User();
        user.setId(1L);
        AuthResponseDto responseDto = new AuthResponseDto();

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(authMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(1L)).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.register(dto);

        assertNotNull(result);
        verify(userRepository, never()).existsByPnumber(any());
    }

    @Test
    void login_success_withEmail() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");

        AuthResponseDto responseDto = new AuthResponseDto();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyLong())).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.login(dto);

        assertNotNull(result);
        verify(userRepository).findByEmail("test@test.com");
        verify(passwordEncoder).matches("123", "encodedPassword");
        verify(jwtService).generateToken(1L);
    }

    @Test
    void login_success_withPhoneNumber() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(null);
        dto.setPNumber("+1234567890");
        dto.setPassword("pass123");

        User user = new User();
        user.setId(2L);
        user.setPnumber("+1234567890");
        user.setPassword("encodedPass");

        AuthResponseDto responseDto = new AuthResponseDto();

        when(userRepository.findByPnumber("+1234567890")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(2L)).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.login(dto);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        verify(userRepository).findByPnumber("+1234567890");
    }

    @Test
    void login_emptyEmail_usesPhoneNumber() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("");
        dto.setPNumber("+1234567890");
        dto.setPassword("pass");

        User user = new User();
        user.setId(2L);
        user.setPassword("encodedPass");

        AuthResponseDto responseDto = new AuthResponseDto();

        when(userRepository.findByPnumber("+1234567890")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(jwtService.generateToken(2L)).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(responseDto);

        AuthResponseDto result = authService.login(dto);

        assertNotNull(result);
        verify(userRepository).findByPnumber("+1234567890");
    }

    @Test
    void login_noCredentials_throwsIllegalArgumentException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(null);
        dto.setPNumber(null);
        dto.setPassword("pass");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login(dto));

        assertEquals("Email or phone number required", ex.getMessage());
    }

    @Test
    void login_emptyEmailAndEmptyPhone_throwsIllegalArgumentException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("");
        dto.setPNumber("");
        dto.setPassword("pass");

        assertThrows(IllegalArgumentException.class, () -> authService.login(dto));
    }

    @Test
    void login_emailNotFound_throwsUserNotFoundException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("notfound@test.com");
        dto.setPassword("123");

        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(dto));
    }

    @Test
    void login_phoneNotFound_throwsUserNotFoundException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail(null);
        dto.setPNumber("+0000000000");
        dto.setPassword("123");

        when(userRepository.findByPnumber("+0000000000")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(dto));
    }

    @Test
    void login_wrongPassword_throwsIllegalArgumentException() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("wrongPassword");

        User user = new User();
        user.setId(1L);
        user.setPassword("encodedCorrectPassword");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedCorrectPassword")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login(dto));

        assertEquals("Invalid password", ex.getMessage());
    }
}