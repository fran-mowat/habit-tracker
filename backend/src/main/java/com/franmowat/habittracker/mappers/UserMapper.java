package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest userRequest){
        User user = new User();

        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(user.getPassword());
        return user;
    }

    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUserName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
