package com.franmowat.habittracker.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
