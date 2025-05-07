package com.example.schedule.conroller;
import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    @Autowired
    public  ScheduleController(ScheduleService scheduleService){
        this.scheduleService=scheduleService;
    }
    @GetMapping
    public List<Schedule> getAllSchedules(){
        return scheduleService.getAllSchedules();
    }
    @GetMapping("/{day}")
    public List<Schedule> getScheduleByDay(@PathVariable DayOfWeek day){
        return scheduleService.getSchedulesByDay(day);
    }
    @PostMapping
    public Schedule addSubject(
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam String subjectName){
      return  scheduleService.addSubject(dayOfWeek,subjectName);
    };

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id){
        scheduleService.deleteSubject(id);
    }






}
