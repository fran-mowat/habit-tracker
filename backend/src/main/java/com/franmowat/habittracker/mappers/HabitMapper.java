package com.franmowat.habittracker.mappers;

import com.franmowat.habittracker.DTOs.HabitRequest;
import com.franmowat.habittracker.DTOs.HabitResponse;
import com.franmowat.habittracker.entities.Habit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitMapper {
    public Habit toEntity(HabitRequest habitRequest){
        Habit habit = new Habit();

        habit.setName(habitRequest.getName());
        habit.setDescription(habitRequest.getDescription());
        habit.setFrequencyUnit(habitRequest.getFrequencyUnit());
        habit.setFrequencyInterval(habitRequest.getFrequencyInterval());
        habit.setFrequencyMetadata(habitRequest.getFrequencyMetadata());
        return habit;
    }

    public HabitResponse toResponse(Habit habit){
        HabitResponse habitResponse = new HabitResponse();

        habitResponse.setHabitId(habit.getHabitId());
        habitResponse.setName(habit.getName());
        habitResponse.setDescription(habit.getDescription());
        habitResponse.setFrequencyUnit(habit.getFrequencyUnit());
        habitResponse.setFrequencyInterval(habit.getFrequencyInterval());
        habitResponse.setFrequencyMetadata(habit.getFrequencyMetadata());
        habitResponse.setCreatedAt(habit.getCreatedAt());
        habitResponse.setUserId(habit.getUser().getUserId());
        return habitResponse;
    }

    public List<HabitResponse> toResponseList(List<Habit> habits){
        return habits.stream().map(this::toResponse).toList();
    }
}
