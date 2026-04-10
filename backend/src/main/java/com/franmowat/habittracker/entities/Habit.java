package com.franmowat.habittracker.entities;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habit")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long habitId;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private FrequencyUnit frequencyUnit;

    private int frequencyInterval;

    @Column(columnDefinition = "jsonb")
    private String frequencyMetadata;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
