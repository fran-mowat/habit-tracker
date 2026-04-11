package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.services.HabitLogService;
import com.franmowat.habittracker.services.HabitService;
import com.franmowat.habittracker.services.UserService;
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
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{userId}/habits")
    public List<Habit> getHabitsByUserId(@PathVariable Long userId){
        return habitService.getHabitsByUserId(userId);
    }

    @GetMapping("/{userId}/habit-logs")
    public List<HabitLog> getHabitLogsByUserId(@PathVariable Long userId){
        return habitLogService.getHabitLogsByUserId(userId);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        user.setUserId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
