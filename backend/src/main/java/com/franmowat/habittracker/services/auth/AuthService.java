package com.franmowat.habittracker.services.auth;

import com.franmowat.habittracker.DTOs.auth.*;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.repositories.UserRepository;
import com.franmowat.habittracker.validation.UserValidationService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserValidationService userValidationService;

    private final String TOKEN_TYPE = "Bearer";

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserValidationService userValidationService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userValidationService = userValidationService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request){
        String email = request.getEmail();
        userValidationService.validateEmail(email);
        if (userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("Email " + email + " already in use");
        }

        String password = request.getPassword();
        userValidationService.validatePassword(password);
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(email);
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return new AuthResponse(
                jwtService.generateToken(user),
                TOKEN_TYPE,
                user.getUserId(),
                user.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (! passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }

        return new AuthResponse(
                jwtService.generateToken(user),
                TOKEN_TYPE,
                user.getUserId(),
                user.getEmail()
        );
    }
}
