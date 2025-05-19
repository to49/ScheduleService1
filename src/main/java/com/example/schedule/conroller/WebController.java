package com.example.schedule.conroller;

import com.example.schedule.model.DayOfWeek;
import com.example.schedule.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Controller
@RequestMapping("/schedule")
public class WebController {
    private final ScheduleService scheduleService;

    public WebController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public String showSchedule(Model model) {
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("scheduleMap", scheduleService.getAllSchedules());
        return "schedule";
    }

    @PostMapping("/add")
    public String addSubject(
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam String subjectName,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time){
        scheduleService.addSubject(dayOfWeek,subjectName,time);
        return "redirect:/schedule";
    }


    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        scheduleService.deleteSubject(id);
        return "redirect:/schedule";
    }
}
