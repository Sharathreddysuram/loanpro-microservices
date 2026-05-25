package com.sharath.loanpro.authservice.service;

import com.sharath.loanpro.authservice.dto.AuthResponseDto;
import com.sharath.loanpro.authservice.dto.LoginRequestDto;
import com.sharath.loanpro.authservice.dto.RegisterRequestDto;
import com.sharath.loanpro.authservice.entity.UserCredential;
import com.sharath.loanpro.authservice.enums.Role;
import com.sharath.loanpro.authservice.repository.UserCredentialRepository;
import com.sharath.loanpro.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDto register(RegisterRequestDto requestDto) {

        if (repository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("User already exists with email: " + requestDto.getEmail());
        }

        UserCredential user = UserCredential.builder()
                .fullName(requestDto.getFullName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .build();

        repository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponseDto.builder()
                .message("User registered successfully")
                .token(token)
                .build();
    }

    public AuthResponseDto login(LoginRequestDto requestDto) {

        UserCredential user = repository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                requestDto.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponseDto.builder()
                .message("Login successful")
                .token(token)
                .build();
    }
}