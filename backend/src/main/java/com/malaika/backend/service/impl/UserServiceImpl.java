package com.malaika.backend.service.impl;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.entity.User;
import com.malaika.backend.exception.UserNotFoundException;
import com.malaika.backend.mapper.UserProfileMapper;
import com.malaika.backend.repository.UserRepository;
import com.malaika.backend.security.CurrentUser;
import com.malaika.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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

        log.info("Fetching profile for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        return userProfileMapper.toDto(user);
    }

    @Override
    public void changePassword(ChangePasswordDto dto) {

        Long userId = currentUser.getCurrentUserId();

        log.info("Password change request for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for password change: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            log.warn("Invalid old password attempt for userId: {}", userId);
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for userId: {}", userId);
    }
}