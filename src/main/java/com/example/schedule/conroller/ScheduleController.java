package com.example.schedule.conroller;
import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.model.Week;
import com.example.schedule.service.ScheduleService;
import com.example.schedule.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final WeekService weekService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, WeekService weekService) {
        this.scheduleService = scheduleService;
        this.weekService = weekService;
    }

    @GetMapping
    public List<Schedule> getAllSchedules(
            @RequestParam(required = false) Integer weekNumber) {

        Week week = (weekNumber != null)
                ? weekService.getWeekByNumber(weekNumber)
                : weekService.getCurrentWeek();

        return scheduleService.getScheduleForWeek(week);
    }

    @GetMapping("/week/{weekNumber}")
    public List<Schedule> getScheduleByWeek(
            @PathVariable int weekNumber) {

        Week week = weekService.getWeekByNumber(weekNumber);
        return scheduleService.getScheduleForWeek(week);
    }

    @GetMapping("/day/{dayOfWeek}")
    public List<Schedule> getScheduleByDay(
            @RequestParam(required = false) Integer weekNumber,
            @PathVariable DayOfWeek dayOfWeek) {

        Week week = (weekNumber != null)
                ? weekService.getWeekByNumber(weekNumber)
                : weekService.getCurrentWeek();

        return scheduleService.getScheduleForWeek(week).stream()
                .filter(s -> s.getDayOfWeek() == dayOfWeek)
                .toList();
    }

    @PostMapping
    public Schedule addSubject(
            @RequestParam int weekNumber,
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam String subjectName,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time) {

        Week week = weekService.getWeekByNumber(weekNumber);
        return scheduleService.addSubject(week, dayOfWeek, subjectName, time);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        scheduleService.deleteSubject(id);
    }

    @GetMapping("/weeks")
    public List<Week> getAllWeeks() {
        return weekService.getAllWeeks();
    }

    @PostMapping("/weeks")
    public Week createWeek(
            @RequestParam String startDate,
            @RequestParam(required = false) Integer weekNumber) {

        LocalDate date = LocalDate.parse(startDate);
        if (weekNumber != null) {
            return weekService.getOrCreateWeek(weekNumber, date);
        }
        return weekService.createNewWeek(date);
    }

    @PostMapping("/weeks/generate")
    public List<Week> generateWeeks(@RequestParam int count) {
        return weekService.generateNextWeeks(count);
    }

    @PostMapping("/copy")
    public String copySchedule(
            @RequestParam int fromWeek,
            @RequestParam int toWeek) {

        scheduleService.copySchedule(fromWeek, toWeek);
        return "Расписание успешно скопировано с недели " + fromWeek + " на неделю " + toWeek;
    }
}