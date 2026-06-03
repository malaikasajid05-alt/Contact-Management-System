package com.malaika.backend.service;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;

public interface UserService {
    ProfileResponseDto getProfile(String email);
    void changePassword(String email , ChangePasswordDto dto);
}
