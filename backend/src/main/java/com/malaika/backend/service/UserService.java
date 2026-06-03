package com.malaika.backend.service;

import com.malaika.backend.dto.ProfileResponseDto;

public interface UserService {
    ProfileResponseDto getProfile(Long id);
}
