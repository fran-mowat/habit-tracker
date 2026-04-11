package com.franmowat.habittracker.DTOs;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
}
