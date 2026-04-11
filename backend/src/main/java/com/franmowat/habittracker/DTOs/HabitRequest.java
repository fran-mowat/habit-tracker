package com.franmowat.habittracker.DTOs;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HabitRequest {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private FrequencyUnit frequencyUnit;

    @Min(1)
    @NotBlank
    private int frequencyInterval;

    private String frequencyMetadata;

    @NotBlank
    private Long userId;
}
