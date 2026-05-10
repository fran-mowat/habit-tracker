package com.franmowat.habittracker.DTOs;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.Optional;

@Data
public class UserRequest {
    private Optional<String> userName;

    @Email
    private String email;

    private Optional<String> password;
}
