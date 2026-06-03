package com.malaika.backend.controller;

import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ProfileResponseDto getProfile(
            @PathVariable Long id) {

        return userService.getProfile(id);
    }
}