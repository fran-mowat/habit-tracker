package com.franmowat.habittracker.services;

import com.franmowat.habittracker.dataTypes.FrequencyUnit;
import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.exceptions.DuplicateResourceException;
import com.franmowat.habittracker.exceptions.HabitNotFoundException;
import com.franmowat.habittracker.repositories.HabitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository){
        this.habitRepository = habitRepository;
    }

    public Habit getHabitById(Long id){
        return habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id " + id));
    }

    public List<Habit> getHabitsByUserId(Long id){
        return habitRepository.findByUser_UserId(id);
    }

    @Transactional
    public Habit createHabit(Habit habit){
        String habitName = habit.getName();
        FrequencyUnit frequencyUnit = habit.getFrequencyUnit();
        int frequencyInterval = habit.getFrequencyInterval();
        Long userId = habit.getUser().getUserId();

        if (habitName == null || habitName.isBlank() ){
            throw new IllegalArgumentException("Habit name field cannot be empty");
        }

        if (habitRepository.existsByUser_UserIdAndName(userId, habitName)){
            throw new DuplicateResourceException("Habit " + habitName + " already exists for user " + userId);
        }

        if (frequencyUnit == null){
            throw new IllegalArgumentException("Frequency unit field cannot be null");
        }

        if (frequencyInterval < 1){
            throw new IllegalArgumentException("Frequency interval field cannot be less than 1");
        }

        return habitRepository.save(habit);
    }

    @Transactional
    public Habit updateHabit(Habit updatedHabit){
        Habit existingHabit = getHabitById(updatedHabit.getHabitId());

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

        return habitRepository.save(existingHabit);
    }

    @Transactional
    public void deleteHabit(Long id){
        Habit habit = getHabitById(id);
        habitRepository.delete(habit);
    }
}
