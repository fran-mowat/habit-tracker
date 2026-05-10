package com.franmowat.habittracker.services.auth;

import com.franmowat.habittracker.exceptions.UserNotFoundException;
import com.franmowat.habittracker.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
    }
}
