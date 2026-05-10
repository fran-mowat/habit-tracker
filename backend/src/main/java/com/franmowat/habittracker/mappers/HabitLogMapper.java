package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.entities.HabitLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitLogMapper {
    private final UserMapper userMapper;
    private final HabitMapper habitMapper;

    public HabitLogMapper(UserMapper userMapper, HabitMapper habitMapper){
        this.userMapper = userMapper;
        this.habitMapper = habitMapper;
    }

    public HabitLogResponse toResponse(HabitLog habitLog){
        HabitLogResponse habitLogResponse = new HabitLogResponse();

        habitLogResponse.setDateCompleted(habitLog.getDateCompleted());
        habitLogResponse.setHabitResponse(habitMapper.toResponse(habitLog.getHabit()));
        return habitLogResponse;
    }

    public List<HabitLogResponse> toResponseList(List<HabitLog> habitLogs){
        return habitLogs.stream().map(this::toResponse).toList();
    }
}
