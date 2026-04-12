package com.franmowat.habittracker.validation;

public class UserValidationService {
    public void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public void validatePassword(String password){
        if (password == null || password.length() < 7){
            throw new IllegalArgumentException("Password field must be longer than 7 characters");
        }

        if (! password.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Password field must contain an uppercase character");
        }

        if (! password.matches(".*[a-z].*")){
            throw new IllegalArgumentException("Password field must contain a lowercase character");
        }

        if (! password.matches(".*[1-9].*")){
            throw new IllegalArgumentException("Password field must contain a digit");
        }

        if (! password.matches(".*[^a-zA-Z0-9].*")){
            throw new IllegalArgumentException("Password field must contain a special character");
        }

        if (password.contains(" ")){
            throw new IllegalArgumentException(("Password field cannot contain spaces"));
        }
    }
}
