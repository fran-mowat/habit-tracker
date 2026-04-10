package com.franmowat.habittracker.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habit_log")
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long habitLogId;

    private LocalDateTime dateCompleted;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @PrePersist
    protected void onCreate() {
        this.dateCompleted = LocalDateTime.now();
    }
}
