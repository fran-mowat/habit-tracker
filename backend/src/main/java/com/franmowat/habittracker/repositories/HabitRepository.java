package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser_UserId(Long userId);
    Boolean existsByUser_UserIdAndName(Long userId, String habitName);
}