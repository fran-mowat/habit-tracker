package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.HabitRequest;
import com.franmowat.habittracker.DTOs.HabitResponse;
import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitNotFoundException;
import com.franmowat.habittracker.mappers.HabitMapper;
import com.franmowat.habittracker.repositories.HabitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    private final UserService userService;
    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    public HabitService(UserService userService, HabitRepository habitRepository, HabitMapper habitMapper){
        this.userService = userService;
        this.habitRepository = habitRepository;
        this.habitMapper = habitMapper;
    }

    public List<HabitResponse> getAllHabits(){
        List<Habit> habits = habitRepository.findAll();
        return habitMapper.toResponseList(habits);
    }

    public Habit getHabitById(Long id){
        return habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id " + id));
    }

    public HabitResponse getHabitByIdResponse(Long id){
        Habit habit = getHabitById(id);
        return habitMapper.toResponse(habit);
    }

    public List<HabitResponse> getHabitsByUserId(Long id){
        List<Habit> habits = habitRepository.findByUser_UserId(id);
        return habitMapper.toResponseList(habits);
    }

    @Transactional
    public HabitResponse createHabit(HabitRequest habitRequest){
        Habit habit = habitMapper.toEntity(habitRequest);

        String habitName = habit.getName();
        FrequencyUnit frequencyUnit = habit.getFrequencyUnit();
        int frequencyInterval = habit.getFrequencyInterval();

        User user = userService.getUserById(habitRequest.getUserId());
        habit.setUser(user);

        if (habitName == null || habitName.isBlank() ){
            throw new IllegalArgumentException("Habit name field cannot be empty");
        }

        if (habitRepository.existsByUser_UserIdAndName(user.getUserId(), habitName)){
            throw new DuplicateResourceException("Habit " + habitName + " already exists for user " + user.getUserId());
        }

        if (frequencyUnit == null){
            throw new IllegalArgumentException("Frequency unit field cannot be null");
        }

        if (frequencyInterval < 1){
            throw new IllegalArgumentException("Frequency interval field cannot be less than 1");
        }

        Habit saved = habitRepository.save(habit);
        return habitMapper.toResponse(saved);
    }

    @Transactional
    public HabitResponse updateHabit(Long id, HabitRequest updatedHabitRequest){
        Habit updatedHabit = habitMapper.toEntity(updatedHabitRequest);

        Habit existingHabit = getHabitById(id);

        String habitName = updatedHabit.getName();
        FrequencyUnit frequencyUnit = updatedHabit.getFrequencyUnit();
        int frequencyInterval = updatedHabit.getFrequencyInterval();
        Long userId = existingHabit.getUser().getUserId();

        if (habitName == null || habitName.isBlank() ){
            throw new IllegalArgumentException("Habit name field cannot be empty");
        }

        boolean nameExists = habitRepository.existsByUser_UserIdAndName(userId, habitName);
        if (nameExists && !existingHabit.getName().equals(habitName)){
            throw new DuplicateResourceException("Habit " + habitName + " already exists for user " + userId);
        }

        if (frequencyUnit == null){
            throw new IllegalArgumentException("Frequency unit field cannot be null");
        }

        if (frequencyInterval < 1){
            throw new IllegalArgumentException("Frequency interval field cannot be less than 1");
        }

        existingHabit.setName(habitName);
        existingHabit.setDescription(updatedHabit.getDescription());
        existingHabit.setFrequencyUnit(frequencyUnit);
        existingHabit.setFrequencyInterval(frequencyInterval);
        existingHabit.setFrequencyMetadata(updatedHabit.getFrequencyMetadata());

        Habit saved = habitRepository.save(existingHabit);
        return habitMapper.toResponse(saved);
    }

    @Transactional
    public void deleteHabit(Long id){
        Habit habit = getHabitById(id);
        habitRepository.delete(habit);
    }
}
