package com.malaika.backend.service;

import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.dto.LoginRequestDto;
import com.malaika.backend.dto.RegisterRequestDto;

public interface AuthService {
       AuthResponseDto register(RegisterRequestDto dto);
       AuthResponseDto login(LoginRequestDto dto);

}
