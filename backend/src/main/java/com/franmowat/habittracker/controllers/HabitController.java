package com.franmowat.habittracker.controllers;

import com.franmowat.habittracker.DTOs.HabitLogResponse;
import com.franmowat.habittracker.DTOs.HabitRequest;
import com.franmowat.habittracker.DTOs.HabitResponse;
import com.franmowat.habittracker.services.HabitLogService;
import com.franmowat.habittracker.services.HabitService;
import jakarta.validation.Valid;
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
    public HabitResponse getHabitById(@PathVariable Long id){
        return habitService.getHabitByIdResponse(id);
    }

    @GetMapping("/{id}/logs")
    public List<HabitLogResponse> getHabitLogsByHabitId(@PathVariable Long id){
        return habitLogService.getHabitLogsByHabitId(id);
    }

    @PostMapping
    public HabitResponse createHabit(@Valid @RequestBody HabitRequest habitRequest){
        return habitService.createHabit(habitRequest);
    }

    @PostMapping("/{id}/logs")
    public HabitLogResponse createHabitLog(@PathVariable Long id){
        return habitLogService.createHabitLog(id);
    }

    @PutMapping("/{id}")
    public HabitResponse updateHabit(@PathVariable Long id, @Valid @RequestBody HabitRequest habitRequest){
        return habitService.updateHabit(id, habitRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable Long id){
        habitService.deleteHabit(id);
    }
}
