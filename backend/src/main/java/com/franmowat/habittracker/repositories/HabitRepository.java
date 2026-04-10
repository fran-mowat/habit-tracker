package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> { }
