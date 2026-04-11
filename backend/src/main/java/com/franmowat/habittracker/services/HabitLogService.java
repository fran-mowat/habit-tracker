package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitLogNotFoundException;
import com.franmowat.habittracker.mappers.HabitLogMapper;
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
    private final HabitLogMapper habitLogMapper;

    public HabitLogService(
            HabitLogRepository habitLogRepository,
            HabitService habitService,
            HabitLogMapper habitLogMapper
    ){
        this.habitLogRepository = habitLogRepository;
        this.habitService = habitService;
        this.habitLogMapper = habitLogMapper;
    }

    private HabitLog getHabitLogById(Long id){
        return habitLogRepository.findById(id)
                .orElseThrow(() -> new HabitLogNotFoundException("Habit log not found with id " + id));
    }

    public HabitLogResponse getHabitLogByIdResponse(Long id){
        HabitLog habitLog = getHabitLogById(id);
        return habitLogMapper.toResponse(habitLog);
    }

    public List<HabitLogResponse> getHabitLogsByUserId(Long id){
        List<HabitLog> habitLogs = habitLogRepository.findByHabit_User_UserId(id);
        return habitLogMapper.toResponseList(habitLogs);
    }

    public List<HabitLogResponse> getHabitLogsByHabitId(Long id){
        List<HabitLog> habitLogs = habitLogRepository.findByHabit_HabitId(id);
        return habitLogMapper.toResponseList(habitLogs);
    }

    public List<HabitLogResponse> getHabitLogsByDate(LocalDate date){
        List<HabitLog> habitLogs = habitLogRepository.findByDateCompletedBetween(
                date.atStartOfDay(),
                date.atTime(23, 59, 59)
        );
        return habitLogMapper.toResponseList(habitLogs);
    }

    @Transactional
    public HabitLogResponse createHabitLog(Long habitId){
        Habit habit = habitService.getHabitById(habitId);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDate = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        if (habitLogRepository.existsByHabit_HabitIdAndDateCompletedBetween(habitId, startOfDate, endOfDay)) {
            throw new DuplicateResourceException("Habit already logged today");
        }

        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);

        HabitLog saved = habitLogRepository.save(habitLog);
        return habitLogMapper.toResponse(saved);
    }

    @Transactional
    public void deleteHabitLog(Long habitLogId){
        HabitLog habitLog = getHabitLogById(habitLogId);
        habitLogRepository.delete(habitLog);
    }
}
