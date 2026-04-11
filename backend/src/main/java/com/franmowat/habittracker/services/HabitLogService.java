package com.franmowat.habittracker.services;

import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitLogNotFoundException;
import com.franmowat.habittracker.repositories.HabitLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabitLogService {
    private final HabitLogRepository habitLogRepository;
    private final HabitService habitService;

    public HabitLogService(
            HabitLogRepository habitLogRepository,
            HabitService habitService
    ){
        this.habitLogRepository = habitLogRepository;
        this.habitService = habitService;
    }

    public HabitLog getHabitLogById(Long id){
        return habitLogRepository.findById(id)
                .orElseThrow(() -> new HabitLogNotFoundException("Habit log not found with id " + id));
    }

    public List<HabitLog> getHabitLogsByUserId(Long id){
        return habitLogRepository.findByHabit_User_UserId(id);
    }

    public List<HabitLog> getHabitLogsByHabitId(Long id){
        return habitLogRepository.findByHabit_HabitId(id);
    }

    public List<HabitLog> getHabitLogsByDate(LocalDate date){
        return habitLogRepository.findByDateCompletedBetween(
                date.atStartOfDay(),
                date.atTime(23, 59, 59)
        );
    }

    @Transactional
    public HabitLog createHabitLog(Long habitId){
        Habit habit = habitService.getHabitById(habitId);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDate = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        if (habitLogRepository.existsByHabit_HabitIdAndDateCompletedBetween(habitId, startOfDate, endOfDay)) {
            throw new DuplicateResourceException("Habit already logged today");
        }

        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);

        return habitLogRepository.save(habitLog);
    }

    @Transactional
    public void deleteHabitLog(Long habitLogId){
        HabitLog habitLog = getHabitLogById(habitLogId);
        habitLogRepository.delete(habitLog);
    }
}
