package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.entities.HabitLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitLogMapper {
    public HabitLogResponse toResponse(HabitLog habitLog){
        HabitLogResponse habitLogResponse = new HabitLogResponse();

        habitLogResponse.setHabitLogId(habitLog.getHabitLogId());
        habitLogResponse.setDateCompleted(habitLog.getDateCompleted());
        habitLogResponse.setHabitId(habitLog.getHabit().getHabitId());
        habitLogResponse.setUserId(habitLog.getHabit().getUser().getUserId());
        return habitLogResponse;
    }

    public List<HabitLogResponse> toResponseList(List<HabitLog> habitLogs){
        return habitLogs.stream().map(this::toResponse).toList();
    }
}
