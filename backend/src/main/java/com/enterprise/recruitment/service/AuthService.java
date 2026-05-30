package com.enterprise.recruitment.service;

import com.enterprise.recruitment.dto.AuthResponse;
import com.enterprise.recruitment.dto.LoginRequest;
import com.enterprise.recruitment.dto.RegisterRequest;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.repository.AppUserRepository;
import com.enterprise.recruitment.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser user = new AppUser(
                request.fullName().trim(),
                email,
                passwordEncoder.encode(request.password()),
                request.role()
        );

        AppUser savedUser = appUserRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        return toAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password())
        );

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtService.generateToken(user);
        return toAuthResponse(user, token);
    }

    public AuthResponse.UserSummary currentUser(AppUser user) {
        return toUserSummary(user);
    }

    private AuthResponse toAuthResponse(AppUser user, String token) {
        return new AuthResponse(token, toUserSummary(user));
    }

    private AuthResponse.UserSummary toUserSummary(AppUser user) {
        return new AuthResponse.UserSummary(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
