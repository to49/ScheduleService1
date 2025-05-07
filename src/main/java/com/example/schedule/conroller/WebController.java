package com.example.schedule.conroller;

import com.example.schedule.model.DayOfWeek;
import com.example.schedule.service.ScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String subjectName) {
        scheduleService.addSubject(dayOfWeek, subjectName);
        return "redirect:/schedule";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        scheduleService.deleteSubject(id);
        return "redirect:/schedule";
    }
}
