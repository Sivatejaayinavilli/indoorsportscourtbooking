package com.example.sportsbooking.service;

import com.example.sportsbooking.dto.AuthResponse;
import com.example.sportsbooking.dto.LoginRequest;
import com.example.sportsbooking.dto.RegisterRequest;
import com.example.sportsbooking.entity.User;
import com.example.sportsbooking.enums.Role;
import com.example.sportsbooking.repository.UserRepository;
import com.example.sportsbooking.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserService userService,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, boolean admin) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role role = admin ? Role.ADMIN : Role.USER;
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPassword(), role);
        String token = generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = generateToken(user);
        return new AuthResponse(token);
    }

    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        return jwtService.generateToken(user.getEmail(), claims);
    }
}