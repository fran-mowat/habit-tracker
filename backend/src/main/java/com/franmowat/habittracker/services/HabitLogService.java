package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitLogNotFoundException;
import com.franmowat.habittracker.mappers.HabitLogMapper;
import com.franmowat.habittracker.repositories.HabitLogRepository;
import com.franmowat.habittracker.services.auth.AuthService;
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
    private final AuthService authService;

    public HabitLogService(
            HabitLogRepository habitLogRepository,
            HabitService habitService,
            HabitLogMapper habitLogMapper,
            AuthService authService
    ){
        this.habitLogRepository = habitLogRepository;
        this.habitService = habitService;
        this.habitLogMapper = habitLogMapper;
        this.authService = authService;
    }

    private HabitLog getHabitLogById(Long id){
        User user = authService.getAuthenticatedUser();
        HabitLog habitLog = habitLogRepository.findById(id)
                .orElseThrow(() -> new HabitLogNotFoundException("Habit log not found with id " + id));;

        if (!habitLog.getHabit().getUser().getUserId().equals(user.getUserId())){
            throw new HabitLogNotFoundException("Habit log not found with id " + id);
        }

        return habitLog;
    }

    public HabitLogResponse getHabitLogByIdResponse(Long id){
        HabitLog habitLog = getHabitLogById(id);
        return habitLogMapper.toResponse(habitLog);
    }

    public List<HabitLogResponse> getAllHabitLogs(){
        User user = authService.getAuthenticatedUser();
        List<HabitLog> habitLogs = habitLogRepository.findByHabit_User_UserId(user.getUserId());
        return habitLogMapper.toResponseList(habitLogs);
    }

    public List<HabitLogResponse> getHabitLogsByHabitId(Long id){
        Habit habit = habitService.getHabitById(id);

        List<HabitLog> habitLogs =
                habitLogRepository.findByHabit_HabitId(habit.getHabitId());

        return habitLogMapper.toResponseList(habitLogs);
    }

    public List<HabitLogResponse> getHabitLogsByDate(LocalDate date){
        User user = authService.getAuthenticatedUser();
        List<HabitLog> habitLogs = habitLogRepository.findByHabit_User_UserIdAndDateCompletedBetween(
                user.getUserId(),
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
