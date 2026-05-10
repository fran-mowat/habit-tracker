package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser_UserId(Long userId);
    Optional<Habit> findByHabitIdAndUser_UserId(Long habitId, Long userId);
    Boolean existsByUser_UserIdAndName(Long userId, String habitName);
}