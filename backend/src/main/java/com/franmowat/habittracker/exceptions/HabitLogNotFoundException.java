package com.franmowat.habittracker.exceptions;

public class HabitLogNotFoundException extends RuntimeException {
    public HabitLogNotFoundException(String message){
        super(message);
    }
}
