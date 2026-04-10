package com.franmowat.habittracker.entities;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habit")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NotBlank
    private Long habitId;

    @Column(nullable = false)
    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private FrequencyUnit frequencyUnit;

    @Column(nullable = false)
    @Min(1)
    @NotBlank
    private int frequencyInterval;

    @Column(columnDefinition = "jsonb")
    private String frequencyMetadata;

    @Column(nullable = false)
    @NotBlank
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotBlank
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
