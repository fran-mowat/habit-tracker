package com.franmowat.habittracker.entities;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long habitId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FrequencyUnit frequencyUnit;

    @Column(nullable = false)
    private int frequencyInterval;

    @Column(columnDefinition = "jsonb")
    private String frequencyMetadata;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
