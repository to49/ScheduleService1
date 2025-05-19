package com.example.schedule.conroller;

import com.example.schedule.model.Week;
import com.example.schedule.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weeks")
public class WeekController {
    @Autowired
    private WeekService weekService;

    @GetMapping
    public List<Week> getAllWeeks() {
        return weekService.getAllWeeks();
    }

    @PostMapping
    public Week createWeek(@RequestParam int weekNumber,
                           @RequestParam String startDate) {
        return weekService.getOrCreateWeek(weekNumber, LocalDate.parse(startDate));
    }
}
