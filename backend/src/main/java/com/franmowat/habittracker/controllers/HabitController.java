package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.entities.Habit;
import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.services.HabitLogService;
import com.franmowat.habittracker.services.HabitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {
    private final HabitService habitService;
    private final HabitLogService habitLogService;

    public HabitController(HabitService habitService, HabitLogService habitLogService){
        this.habitService = habitService;
        this.habitLogService = habitLogService;
    }

    @GetMapping("/{id}")
    public Habit getHabitById(@PathVariable Long id){
        return habitService.getHabitById(id);
    }

    @GetMapping("/{id}/logs")
    public List<HabitLog> getHabitLogsByHabitId(@PathVariable Long id){
        return habitLogService.getHabitLogsByHabitId(id);
    }

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit){
        return habitService.createHabit(habit);
    }

    @PostMapping("/{id}/logs")
    public HabitLog createHabitLog(@PathVariable Long id){
        return habitLogService.createHabitLog(id);
    }

    @PutMapping("/{id}")
    public Habit updateHabit(@PathVariable Long id, @RequestBody Habit habit){
        habit.setHabitId(id);
        return habitService.updateHabit(habit);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id){
        habitService.deleteHabit(id);
    }
}
