package com.bipul.ems.service;

import com.bipul.ems.dto.AuthDTO.AuthResponse;
import com.bipul.ems.dto.AuthDTO.LoginRequest;
import com.bipul.ems.dto.AuthDTO.RegisterRequest;
import com.bipul.ems.model.User;
import com.bipul.ems.repository.UserRepository;
import com.bipul.ems.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest request) {
        log.debug("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // BCrypt password hashing
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_USER);

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());

        return new AuthResponse(null, request.getUsername(),
                User.Role.ROLE_USER.name(), "User registered successfully!");
    }

    public AuthResponse login(LoginRequest request) {
        log.debug("User login attempt: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        log.info("User logged in successfully: {}", request.getUsername());

        return new AuthResponse(token, user.getUsername(),
                user.getRole().name(), "Login successful!");
    }
}
