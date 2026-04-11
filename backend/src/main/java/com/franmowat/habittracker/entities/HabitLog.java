package com.franmowat.habittracker.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habit_logs")
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long habitLogId;

    @Column(nullable = false)
    private LocalDateTime dateCompleted;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @PrePersist
    protected void onCreate() {
        this.dateCompleted = LocalDateTime.now();
    }
}
