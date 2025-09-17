package com.example.inventory.auth;

import com.example.inventory.auth.dto.AuthDtos.*;
import com.example.inventory.security.JwtService;
import com.example.inventory.security.RevokedToken;
import com.example.inventory.security.RevokedTokenRepository;
import com.example.inventory.user.Role;
import com.example.inventory.user.User;
import com.example.inventory.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwtService,
                       RevokedTokenRepository revokedTokenRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
    }

    @Transactional
    public MessageResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username))
            return new MessageResponse("Username already exists");
        if (userRepository.existsByEmail(req.email))
            return new MessageResponse("Email already exists");
        User u = new User();
        u.setUsername(req.username);
        u.setEmail(req.email);
        u.setPassword(encoder.encode(req.password));
        if ("ADMIN".equalsIgnoreCase(req.role)) {
            u.setRole(Role.ADMIN);
        }
        userRepository.save(u);
        return new MessageResponse("Registered");
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username, req.password));
        User u = userRepository.findByUsername(req.username).orElseThrow();
        String token = jwtService.generateToken(u.getUsername(), u.getRole().name());
        return new AuthResponse(token);
    }

    public MessageResponse logout(String token) {
        if (token != null && !token.isBlank()) {
            revokedTokenRepository.save(new RevokedToken(token, jwtService.getExpiry(token)));
        }
        return new MessageResponse("Logged out");
    }
}
