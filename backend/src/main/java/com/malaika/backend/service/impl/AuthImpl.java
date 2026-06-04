package com.malaika.backend.service.impl;

import com.malaika.backend.dto.LoginRequestDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.AuthMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.JwtService;
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
    private final JwtService jwtService;
    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        if (dto.getEmail() != null &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (dto.getPnumber() != null &&
                userRepository.existsByPnumber(dto.getPnumber())) {
            throw new IllegalArgumentException("Phone Number already registered");
        }

        User user = authMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getId());

        AuthResponseDto responseDto = authMapper.toDto(savedUser);
        responseDto.setToken(token);

        return responseDto;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        User user;
        if(dto.getEmail()!=null && !dto.getEmail().isEmpty() ){
            user= userRepository.findByEmail(dto.getEmail()).
                    orElseThrow(()->new IllegalArgumentException("Invalid email"));
        }
        else if(dto.getPNumber()!=null && !dto.getPNumber().isEmpty() ){
            user= userRepository.findByPnumber(dto.getPNumber()).
                    orElseThrow(()->new IllegalArgumentException("Invalid Phone Number"));
        }
        else
            throw new IllegalArgumentException("Invalid email or phone number");

        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtService.generateToken(user.getId());
        AuthResponseDto responseDto = authMapper.toDto(user);
        responseDto.setToken(token);

        return responseDto;
    }

}
