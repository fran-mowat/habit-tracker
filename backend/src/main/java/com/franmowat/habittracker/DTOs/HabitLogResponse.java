package com.franmowat.habittracker.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitLogResponse {
    private Long habitLogId;
    private LocalDateTime dateCompleted;
    private Long habitId;
    private Long userId;
}
