package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public UserResponse getUser(){
        return userService.getUser();
    }

    @PutMapping
    public UserResponse updateUser(@Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(userRequest);
    }

    @DeleteMapping
    public void deleteUser(){
        userService.deleteUser();
    }
}
