package com.example.schedule.conroller;

import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.model.Week;
import com.example.schedule.service.ScheduleService;
import com.example.schedule.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
@RequestMapping("/schedule")
public class WebController {
    private final ScheduleService scheduleService;
    private final WeekService weekService;

    @Autowired
    public WebController(ScheduleService scheduleService, WeekService weekService) {
        this.scheduleService = scheduleService;
        this.weekService = weekService;
    }

    @GetMapping
    public String showSchedule(
            @RequestParam(required = false) Integer weekNumber,
            Model model) {

        Week currentWeek = (weekNumber != null)
                ? weekService.getWeekByNumber(weekNumber)
                : weekService.getCurrentWeek();

        model.addAttribute("currentWeek", currentWeek);
        model.addAttribute("weeks", weekService.getAllWeeks());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("scheduleMap", scheduleService.getScheduleForWeek(currentWeek));

        return "schedule";
    }

    @GetMapping("/manage-weeks")
    public String manageWeeks(Model model) {
        model.addAttribute("weeks", weekService.getAllWeeks());
        return "manage-weeks";
    }

    @PostMapping("/add")
    public String addSubject(
            @RequestParam int weekNumber,
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam String subjectName,
            @RequestParam String time) {

        Week week = weekService.getWeekByNumber(weekNumber);
        scheduleService.addSubject(week, dayOfWeek, subjectName, LocalTime.parse(time));

        return "redirect:/schedule?weekNumber=" + weekNumber;
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(
            @PathVariable Long id,
            @RequestParam(required = false) Integer weekNumber) {

        scheduleService.deleteSubject(id);
        return "redirect:/schedule" + (weekNumber != null ? "?weekNumber=" + weekNumber : "");
    }

    @PostMapping("/create-week")
    public String createWeek(@RequestParam String startDate) {
        weekService.createNewWeek(LocalDate.parse(startDate));
        return "redirect:/schedule/manage-weeks";
    }

    @PostMapping("/generate-weeks")
    public String generateWeeks(@RequestParam int count) {
        weekService.generateNextWeeks(count);
        return "redirect:/schedule/manage-weeks";
    }

    @PostMapping("/copy-schedule")
    public String copySchedule(
            @RequestParam int fromWeek,
            @RequestParam int toWeek) {

        scheduleService.copySchedule(fromWeek, toWeek);
        return "redirect:/schedule?weekNumber=" + toWeek;
    }
}