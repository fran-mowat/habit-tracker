package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setUserName(user.getUserNameField());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
