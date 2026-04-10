package com.franmowat.habittracker.repositories;

import com.franmowat.habittracker.entities.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> { }
