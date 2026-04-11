package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;

import java.time.LocalDateTime;

public class HabitResponse {
    private Long habitId;
    private String name;
    private String description;
    private FrequencyUnit frequencyUnit;
    private int frequencyInterval;
    private String frequencyMetadata;
    private LocalDateTime createdAt;
    private Long userId;
}
