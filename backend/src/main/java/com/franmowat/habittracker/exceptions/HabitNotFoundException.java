package com.franmowat.habittracker.exceptions;

public class HabitNotFoundException extends RuntimeException{
    public HabitNotFoundException(String message){
        super(message);
    }
}
