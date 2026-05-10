package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.mappers.UserMapper;
import com.franmowat.habittracker.repositories.UserRepository;
import com.franmowat.habittracker.services.auth.AuthService;
import com.franmowat.habittracker.validation.UserValidationService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            UserValidationService userValidationService,
            PasswordEncoder passwordEncoder,
            AuthService authService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    public UserResponse getUser(){
        User user = authService.getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UserRequest updatedUserRequest) {
        User user = authService.getAuthenticatedUser();

        Optional<String> userName = updatedUserRequest.getUserName();
        if (userName != null && userName.isPresent()){
            userValidationService.validateUserName(userName.get());
            user.setUserName(userName.get());
        }

        Optional<String> email = Optional.ofNullable(updatedUserRequest.getEmail());
        if (email.isPresent()){
            userValidationService.validateEmail(email.get());
            user.setEmail(email.get());
        }

        Optional<String> password = updatedUserRequest.getPassword();
        if (password != null && password.isPresent()){
            userValidationService.validatePassword(password.get());
            String hashedPassword = passwordEncoder.encode(password.get());
            user.setPassword(hashedPassword);
        }

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public void deleteUser() {
        User user = authService.getAuthenticatedUser();
        userRepository.delete(user);
    }
}
