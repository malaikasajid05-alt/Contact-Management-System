package com.malaika.backend.service;

import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.RegisterResponseDto;

public interface AuthService {
       RegisterResponseDto register(RegisterRequestDto dto);
}
