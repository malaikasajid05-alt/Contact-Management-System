package com.malaika.backend.controller;

import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.RegisterResponseDto;
import com.malaika.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponseDto register(
            @Valid @RequestBody RegisterRequestDto request
    ) {

        return authService.register(request);
    }

}