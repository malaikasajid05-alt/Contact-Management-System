package com.malaika.backend.controller;

import com.malaika.backend.dto.ChangePasswordDto;
import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ProfileResponseDto getProfile() {
        return userService.getProfile();
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return "Password changed successfully";
    }
}