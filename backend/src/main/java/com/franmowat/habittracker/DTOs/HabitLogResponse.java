package com.franmowat.habittracker.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitLogResponse {
    private LocalDateTime dateCompleted;
    private HabitResponse habitResponse;
}
