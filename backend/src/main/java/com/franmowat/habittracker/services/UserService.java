package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.UserNotFoundException;
import com.franmowat.habittracker.mappers.UserMapper;
import com.franmowat.habittracker.repositories.UserRepository;
import com.franmowat.habittracker.validation.UserValidationService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            UserValidationService userValidationService,
            PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    public UserResponse getUserByIdResponse(Long id){
        User user = getUserById(id);
        return userMapper.toResponse(user);
    }

    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest updatedUserRequest) {
        User updatedUser = userMapper.toEntity(updatedUserRequest);
        User existingUser = getUserById(id);

        String password = updatedUser.getPassword();
        userValidationService.validatePassword(password);
        String hashedPassword = passwordEncoder.encode(password);

        existingUser.setUserName(updatedUser.getUserNameField());
        existingUser.setPassword(hashedPassword);

        User saved = userRepository.save(existingUser);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


}
