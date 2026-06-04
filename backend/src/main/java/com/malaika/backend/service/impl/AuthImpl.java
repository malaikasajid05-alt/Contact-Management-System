package com.malaika.backend.service.impl;

import com.malaika.backend.dto.LoginRequestDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.AuthMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.JwtService;
import com.malaika.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        log.info("Register request received for email: {}", dto.getEmail());

        if (dto.getEmail() != null &&
                userRepository.existsByEmail(dto.getEmail())) {

            log.warn("Email already registered: {}", dto.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        if (dto.getPnumber() != null &&
                userRepository.existsByPnumber(dto.getPnumber())) {

            log.warn("Phone number already registered: {}", dto.getPnumber());
            throw new IllegalArgumentException("Phone Number already registered");
        }

        User user = authMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id: {}", savedUser.getId());

        String token = jwtService.generateToken(savedUser.getId());

        AuthResponseDto responseDto = authMapper.toDto(savedUser);
        responseDto.setToken(token);

        return responseDto;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        log.info("Login request received");

        User user;

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {

            log.info("Login using email: {}", dto.getEmail());

            user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> {
                        log.error("Invalid email login attempt: {}", dto.getEmail());
                        return new UserNotFoundException("Invalid email or user not found");
                    });

        } else if (dto.getPNumber() != null && !dto.getPNumber().isEmpty()) {

            log.info("Login using phone: {}", dto.getPNumber());

            user = userRepository.findByPnumber(dto.getPNumber())
                    .orElseThrow(() -> {
                        log.error("Invalid phone login attempt: {}", dto.getPNumber());
                        return new UserNotFoundException("Invalid phone number or user not found");
                    });

        } else {
            log.error("Login attempt without credentials");
            throw new IllegalArgumentException("Email or phone number required");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.error("Invalid password attempt for user: {}", user.getId());
            throw new IllegalArgumentException("Invalid password");
        }

        log.info("User logged in successfully: {}", user.getId());

        String token = jwtService.generateToken(user.getId());

        AuthResponseDto responseDto = authMapper.toDto(user);
        responseDto.setToken(token);

        return responseDto;
    }
}