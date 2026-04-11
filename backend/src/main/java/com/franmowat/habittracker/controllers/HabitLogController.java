package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.entities.HabitLog;
import com.franmowat.habittracker.services.HabitLogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habit-logs")
public class HabitLogController {
    private final HabitLogService habitLogService;

    public HabitLogController(HabitLogService habitLogService){
        this.habitLogService = habitLogService;
    }

    @GetMapping("/{id}")
    public HabitLog getHabitLog(@PathVariable Long id){
        return habitLogService.getHabitLogById(id);
    }

    @GetMapping
    public List<HabitLog> getHabitLogsByDate(@RequestParam LocalDate date){
        return habitLogService.getHabitLogsByDate(date);
    }

    @DeleteMapping("/{id}")
    public void deleteHabitLog(@PathVariable Long id){
        habitLogService.deleteHabitLog(id);
    }
}
