package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class HabitResponse {
    private Long habitId;
    private String name;
    private String description;
    private FrequencyUnit frequencyUnit;
    private int frequencyInterval;
    private Map<String, Object> frequencyMetadata;
    private LocalDateTime createdAt;
    private Long userId;
}
