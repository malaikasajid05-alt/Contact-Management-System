package com.malaika.backend.service.impl;

import com.malaika.backend.dto.LoginRequestDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.AuthMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.service.AuthService;
import com.malaika.backend.service.JwtService;
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

        if(userRepository.existsByEmail(dto.getEmail()) && dto.getEmail()!=null){
            throw new IllegalArgumentException("Email already registered");
        }
        if(userRepository.existsByPnumber(dto.getPnumber()) && dto.getPnumber()!=null){
            throw new IllegalArgumentException("Phone Number already registered");
        }

        User user = authMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        AuthResponseDto responseDto = authMapper.toDto(user);
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
        
        String token = jwtService.generateToken(user.getEmail());
        AuthResponseDto responseDto = authMapper.toDto(user);
        responseDto.setToken(token);

        return responseDto;
    }

}
