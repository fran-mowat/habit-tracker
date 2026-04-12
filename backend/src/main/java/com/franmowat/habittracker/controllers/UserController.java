package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.DTOs.HabitResponse;
import com.franmowat.habittracker.DTOs.UserRequest;
import com.franmowat.habittracker.DTOs.UserResponse;
import com.franmowat.habittracker.services.HabitLogService;
import com.franmowat.habittracker.services.HabitService;
import com.franmowat.habittracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final HabitService habitService;
    private final HabitLogService habitLogService;

    public UserController(UserService userService, HabitService habitService, HabitLogService habitLogService){
        this.userService = userService;
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserByIdResponse(id);
    }

    @GetMapping("/email/{email}")
    public UserResponse getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{userId}/habits")
    public List<HabitResponse> getHabitsByUserId(@PathVariable Long userId){
        return habitService.getHabitsByUserId(userId);
    }

    @GetMapping("/{userId}/habit-logs")
    public List<HabitLogResponse> getHabitLogsByUserId(@PathVariable Long userId){
        return habitLogService.getHabitLogsByUserId(userId);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
