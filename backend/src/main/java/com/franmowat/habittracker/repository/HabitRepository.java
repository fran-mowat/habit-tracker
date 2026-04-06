package com.franmowat.habittracker.repository;

import com.franmowat.habittracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> { }
