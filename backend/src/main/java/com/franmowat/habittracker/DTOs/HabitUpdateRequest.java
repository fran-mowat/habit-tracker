package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import lombok.Data;

import java.util.Map;

@Data
public class HabitUpdateRequest {
    private String name;
    private String description;
    private FrequencyUnit frequencyUnit;
    private Integer frequencyInterval;
    private Map<String, Object> frequencyMetadata;
}
