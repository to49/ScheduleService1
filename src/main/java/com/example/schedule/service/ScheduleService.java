package com.example.schedule.service;

import com.example.schedule.model.*;
import com.example.schedule.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Schedule addSubject(Week week, DayOfWeek dayOfWeek, String subjectName, LocalTime time) {
        validateTime(time);
        checkTimeSlotAvailability(week, dayOfWeek, time);

        return scheduleRepository.save(new Schedule(week, dayOfWeek, subjectName, time));
    }

    public List<Schedule> getScheduleForWeek(Week week) {
        return scheduleRepository.findByWeekOrderByDayOfWeekAscTimeAsc(week);
    }

    @Transactional
    public void deleteSubject(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public void copySchedule(int fromWeekNumber, int toWeekNumber) {
        Week fromWeek = weekService.getWeekByNumber(fromWeekNumber);
        Week toWeek = weekService.getWeekByNumber(toWeekNumber);

        List<Schedule> schedules = scheduleRepository.findByWeek(fromWeek);

        schedules.forEach(schedule -> {
            scheduleRepository.save(new Schedule(
                    toWeek,
                    schedule.getDayOfWeek(),
                    schedule.getSubjectName(),
                    schedule.getTime()
            ));
        });
    }

    private void validateTime(LocalTime time) {
        if (time.isBefore(LocalTime.of(8, 0)) || time.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException("Время должно быть между 08:00 и 20:00");
        }
    }

    private void checkTimeSlotAvailability(Week week, DayOfWeek dayOfWeek, LocalTime time) {
        if (scheduleRepository.existsByWeekAndDayOfWeekAndTime(week, dayOfWeek, time)) {
            throw new IllegalArgumentException("На это время уже запланировано занятие");
        }
    }
}