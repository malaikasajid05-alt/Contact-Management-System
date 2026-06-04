package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.mapper.UserProfileMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;

    @Override
    public ProfileResponseDto getProfile() {

        Long userId = currentUser.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userProfileMapper.toDto(user);
    }

    @Override
    public void changePassword(ChangePasswordDto dto) {

        Long userId = currentUser.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}