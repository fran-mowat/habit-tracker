package com.franmowat.habittracker.services.auth;

import com.franmowat.habittracker.DTOs.auth.*;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.UserNotFoundException;
import com.franmowat.habittracker.repositories.UserRepository;
import com.franmowat.habittracker.validation.UserValidationService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String userName = request.getUserName();
        userValidationService.validateUserName(userName);

        String email = request.getEmail();
        userValidationService.validateEmail(email);

        String password = request.getPassword();
        userValidationService.validatePassword(password);
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUserName(userName);
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

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
    }
}
