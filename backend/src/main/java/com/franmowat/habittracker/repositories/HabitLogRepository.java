package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findByHabit_User_UserId(Long id);
    List<HabitLog> findByHabit_HabitId(Long id);
    List<HabitLog> findByDateCompletedBetween(LocalDateTime startTimestamp, LocalDateTime endTimestamp);
    Boolean existsByHabit_HabitIdAndDateCompletedBetween(Long id, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}