package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class HabitResponse {
    private String name;
    private String description;
    private FrequencyUnit frequencyUnit;
    private Integer frequencyInterval;
    private Map<String, Object> frequencyMetadata;
    private LocalDateTime createdAt;
    private UserResponse user;
}
