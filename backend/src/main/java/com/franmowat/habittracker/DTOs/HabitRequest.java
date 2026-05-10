package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class HabitRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private FrequencyUnit frequencyUnit;

    @Min(1)
    @NotNull
    private int frequencyInterval;

    private Map<String, Object> frequencyMetadata;

    @NotNull
    private Long userId;
}
