package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.UserNotFoundException;
import com.franmowat.habittracker.mappers.UserMapper;
import com.franmowat.habittracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);

        String email = user.getEmail();
        String name = user.getUserName();
        String password = user.getPassword();

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name field cannot be empty");
        }

        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email field cannot be empty");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("Email " + email + " already in use");
        }

        validatePassword(password);

        //TO DO: configure password encoder to hash password

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest updatedUserRequest) {
        User updatedUser = userMapper.toEntity(updatedUserRequest);

        User existingUser = getUserById(id);

        String name = updatedUser.getUserName();
        String password = updatedUser.getPassword();

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name field cannot be empty");
        }

        validatePassword(password);

        existingUser.setUserName(name);
        existingUser.setPassword(password);

        User saved = userRepository.save(existingUser);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private void validatePassword(String password){
        if (password == null || password.length() < 7){
            throw new IllegalArgumentException("Password field must be longer than 7 characters");
        }

        if (! password.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Password field must contain an uppercase character");
        }

        if (! password.matches(".*[a-z].*")){
            throw new IllegalArgumentException("Password field must contain a lowercase character");
        }

        if (! password.matches(".*[1-9].*")){
            throw new IllegalArgumentException("Password field must contain a digit");
        }

        if (! password.matches(".*[^a-zA-Z0-9].*")){
            throw new IllegalArgumentException("Password field must contain a special character");
        }

        if (password.contains(" ")){
            throw new IllegalArgumentException(("Password field cannot contain spaces"));
        }
    }
}
