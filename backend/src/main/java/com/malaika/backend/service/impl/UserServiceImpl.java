package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.UserProfileMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;
    @Override
    public ProfileResponseDto getProfile(Long id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            ProfileResponseDto dto =     userProfileMapper.toDto(user);

            return dto;
        }

}
