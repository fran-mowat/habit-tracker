package com.franmowat.habittracker.services.auth;

import com.franmowat.habittracker.DTOs.auth.AuthResponse;
import com.franmowat.habittracker.DTOs.auth.LoginRequest;
import com.franmowat.habittracker.DTOs.auth.RegisterRequest;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String TOKEN_TYPE = "Bearer";

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request){
        String email = request.getEmail();
        if (userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("Email " + email + " already in use");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return new AuthResponse(
                "token", // TO DO: generate JWT token
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
                "", //TO DO: generate JWT token
                TOKEN_TYPE,
                user.getUserId(),
                user.getEmail()
        );
    }
}
