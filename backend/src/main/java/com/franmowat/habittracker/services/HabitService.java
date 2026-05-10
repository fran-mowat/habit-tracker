package com.franmowat.habittracker.services;

import com.franmowat.habittracker.DTOs.HabitRequest;
import com.franmowat.habittracker.DTOs.HabitResponse;
import com.franmowat.habittracker.DTOs.HabitUpdateRequest;
import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.User;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitNotFoundException;
import com.franmowat.habittracker.mappers.HabitMapper;
import com.franmowat.habittracker.repositories.HabitRepository;
import com.franmowat.habittracker.services.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;
    private final AuthService authService;

    public HabitService(
            HabitRepository habitRepository,
            HabitMapper habitMapper,
            AuthService authService){
        this.habitRepository = habitRepository;
        this.habitMapper = habitMapper;
        this.authService = authService;
    }

    public Habit getHabitById(Long id){
        User user = authService.getAuthenticatedUser();
        return habitRepository
                .findByHabitIdAndUser_UserId(id, user.getUserId())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id " + id));
    }

    public HabitResponse getHabitByIdResponse(Long id){
        Habit habit = getHabitById(id);
        return habitMapper.toResponse(habit);
    }

    public List<HabitResponse> getAllHabits(){
        User user = authService.getAuthenticatedUser();
        List<Habit> habits = habitRepository.findByUser_UserId(user.getUserId());
        return habitMapper.toResponseList(habits);
    }

    @Transactional
    public HabitResponse createHabit(HabitRequest habitRequest){
        Habit habit = habitMapper.toEntity(habitRequest);

        User user = authService.getAuthenticatedUser();
        habit.setUser(user);

        validateHabitName(habit.getName(), user.getUserId(), null);
        validateDescription(habit.getDescription());
        validateFrequencyUnit(habit.getFrequencyUnit());
        validateFrequencyInterval(habit.getFrequencyInterval());
        validateFrequencyMetadata(habit.getFrequencyMetadata());

        Habit saved = habitRepository.save(habit);
        return habitMapper.toResponse(saved);
    }

    @Transactional
    public HabitResponse updateHabit(Long id, HabitUpdateRequest updatedHabitRequest){
        Habit existingHabit = getHabitById(id);
        Long userId = existingHabit.getUser().getUserId();

        String habitName = updatedHabitRequest.getName();
        if (habitName != null){
            validateHabitName(habitName, userId, existingHabit.getName());
            existingHabit.setName(habitName);
        }

        String description = updatedHabitRequest.getDescription();
        if (description != null){
            validateDescription(description);
            existingHabit.setDescription(description);
        }

        FrequencyUnit frequencyUnit = updatedHabitRequest.getFrequencyUnit();
        if (frequencyUnit != null) {
            validateFrequencyUnit(frequencyUnit);
            existingHabit.setFrequencyUnit(frequencyUnit);
        }

        Integer frequencyInterval = updatedHabitRequest.getFrequencyInterval();
        if (frequencyInterval != null) {
            validateFrequencyInterval(frequencyInterval);
            existingHabit.setFrequencyInterval(frequencyInterval);
        }

        Map<String, Object> frequencyMetadata = updatedHabitRequest.getFrequencyMetadata();
        if (frequencyMetadata != null){
            validateFrequencyMetadata(frequencyMetadata);
            existingHabit.setFrequencyMetadata(frequencyMetadata);
        }

        Habit saved = habitRepository.save(existingHabit);
        return habitMapper.toResponse(saved);
    }

    @Transactional
    public void deleteHabit(Long id){
        Habit habit = getHabitById(id);
        habitRepository.delete(habit);
    }

    private void validateHabitName(String habitName, Long userId, String existingHabitName){
        if (habitName.length() < 3){
            throw new IllegalArgumentException("Habit name field must be longer than 3 characters");
        }

        boolean nameExists = habitRepository.existsByUser_UserIdAndName(userId, habitName);

        if (existingHabitName != null){
            if (nameExists && !existingHabitName.equals(habitName)){
                throw new DuplicateResourceException("Habit " + habitName + " already exists");
            }
        } else {
            if (nameExists){
                throw new DuplicateResourceException("Habit " + habitName + " already exists");
            }
        }
    }

    private void validateDescription(String description){
        if (description != null && !(description.isEmpty()) && description.length() < 5){
            throw new IllegalArgumentException("Habit name field must be longer than 5 characters");
        }
    }

    private void validateFrequencyUnit(FrequencyUnit frequencyUnit){
        //TO DO: define frequency unit validations
    }

    private void validateFrequencyInterval(Integer frequencyInterval){
        if (frequencyInterval < 1){
            throw new IllegalArgumentException("Frequency interval field cannot be less than 1");
        }
    }

    private void validateFrequencyMetadata(Map<String, Object> frequencyMetadata){
        //TO DO: define frequency metadata validations
    }
}
