package com.franmowat.habittracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habit_log")
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @NotBlank
    private Long habitLogId;

    @Column(nullable = false)
    @NotBlank
    private LocalDateTime dateCompleted;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    @NotBlank
    private Habit habit;

    @PrePersist
    protected void onCreate() {
        this.dateCompleted = LocalDateTime.now();
    }
}
