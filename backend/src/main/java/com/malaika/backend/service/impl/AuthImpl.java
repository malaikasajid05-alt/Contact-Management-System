package com.malaika.backend.service.impl;

import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.RegisterResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.AuthMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public RegisterResponseDto register(RegisterRequestDto dto) {

        if(userRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("Email already registered");
        }

        User user = authMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());

        return authMapper.toDto(user);
    }
}
