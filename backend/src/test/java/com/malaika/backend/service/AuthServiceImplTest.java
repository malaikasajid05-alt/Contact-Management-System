package com.malaika.backend.service;

import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.entity.User;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthMapper authMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthImpl authService;

    @Test
    void register_success() {

        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123");

        User user = new User();

        when(authMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(anyLong())).thenReturn("token");
        when(authMapper.toDto(user)).thenReturn(new AuthResponseDto());

        AuthResponseDto result = authService.register(dto);

        assertNotNull(result);
        verify(userRepository).save(any());
    }
}