package com.sharath.loanpro.authservice.controller;

import com.sharath.loanpro.authservice.dto.AuthResponseDto;
import com.sharath.loanpro.authservice.dto.LoginRequestDto;
import com.sharath.loanpro.authservice.dto.RegisterRequestDto;
import com.sharath.loanpro.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return authService.register(requestDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto);
    }
}