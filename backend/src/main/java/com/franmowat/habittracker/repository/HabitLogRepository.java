package com.franmowat.habittracker.repository;

import com.franmowat.habittracker.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> { }
