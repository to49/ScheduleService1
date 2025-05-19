package com.example.schedule.service;

import com.example.schedule.model.*;
import com.example.schedule.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private WeekService weekService;

    @PostConstruct
    public void init() {
        if (scheduleRepository.count() == 0) {
            Week currentWeek = weekService.getCurrentWeek();

            Arrays.stream(DayOfWeek.values())
                    .filter(day -> !Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day))
                    .forEach(day -> {
                        scheduleRepository.save(new Schedule(
                                currentWeek, day, "Математика", LocalTime.of(9, 0)
                        ));
                        scheduleRepository.save(new Schedule(
                                currentWeek, day, "Физика", LocalTime.of(11, 0)
                        ));
                    });
        }
    }

    public Schedule addSubject(Week week, DayOfWeek dayOfWeek, String subjectName, LocalTime time) {
        if (time.isBefore(LocalTime.of(8, 0)) ){
            throw new IllegalArgumentException("Занятия начинаются не раньше 8:00");
        }
        if (time.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException("Занятия заканчиваются не позже 20:00");
        }
        if (scheduleRepository.existsByWeekAndDayOfWeekAndTime(week, dayOfWeek, time)) {
            throw new IllegalArgumentException("На это время уже запланировано занятие");
        }

        return scheduleRepository.save(new Schedule(week, dayOfWeek, subjectName, time));
    }

    public List<Schedule> getScheduleForWeek(Week week) {
        return scheduleRepository.findByWeekOrderByDayOfWeekAscTimeAsc(week);
    }

    public void deleteSubject(Long id) {
        scheduleRepository.deleteById(id);
    }
}
