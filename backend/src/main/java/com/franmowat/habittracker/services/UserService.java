package com.franmowat.habittracker.services;

import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.UserNotFoundException;
import com.franmowat.habittracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));
    }

    @Transactional
    public User createUser(User user) {
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

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User updatedUser) {
        User existingUser = getUserById(updatedUser.getUserId());

        String name = updatedUser.getUserName();
        String password = updatedUser.getPassword();

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name field cannot be empty");
        }

        validatePassword(password);

        existingUser.setUserName(name);
        existingUser.setPassword(password);

        return userRepository.save(existingUser);
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
