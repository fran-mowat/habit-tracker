package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public void updateEntity(User user, UserRequest userRequest){
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
    }

    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUserNameField());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
