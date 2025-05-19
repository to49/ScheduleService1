package com.example.schedule.service;

import com.example.schedule.model.Week;
import com.example.schedule.repository.WeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WeekService {
    @Autowired
    private WeekRepository weekRepository;

    @Transactional
    public Week getOrCreateWeek(int weekNumber, LocalDate startDate) {
        return weekRepository.findByWeekNumber(weekNumber)
                .orElseGet(() -> {
                    Week newWeek = new Week();
                    newWeek.setWeekNumber(weekNumber);
                    newWeek.setStartDate(startDate);
                    newWeek.setEndDate(startDate.plusDays(6));
                    return weekRepository.save(newWeek);
                });
    }

    public Week getCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return weekRepository.findWeekContainingDate(monday)
                .orElseGet(() -> {
                    int weekNumber = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                    return getOrCreateWeek(weekNumber, monday);
                });
    }

    public Week getWeekByNumber(int weekNumber) {
        return weekRepository.findByWeekNumber(weekNumber)
                .orElseThrow(() -> new IllegalArgumentException("Неделя с номером " + weekNumber + " не найдена"));
    }

    public List<Week> getAllWeeks() {
        return weekRepository.findAllByOrderByWeekNumberAsc();
    }
    //!!!!!1
    @Transactional
    public Week createNewWeek(LocalDate startDate) {
        if (!startDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            throw new IllegalArgumentException("Дата начала недели должна быть понедельником");
        }

        int weekNumber = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());

        if (weekRepository.existsByWeekNumber(weekNumber)) {
            throw new IllegalArgumentException("Неделя с номером " + weekNumber + " уже существует");
        }

        Week week = new Week();
        week.setWeekNumber(weekNumber);
        week.setStartDate(startDate);
        week.setEndDate(startDate.plusDays(6));

        return weekRepository.save(week);
    }

    @Transactional
    public List<Week> generateNextWeeks(int count) {
        Week lastWeek = weekRepository.findTopByOrderByWeekNumberDesc()
                .orElseGet(() -> createNewWeek(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))));

        List<Week> newWeeks = new ArrayList<>();
        LocalDate nextMonday = lastWeek.getEndDate().plusDays(1);

        for (int i = 0; i < count; i++) {
            Week week = createNewWeek(nextMonday);
            newWeeks.add(week);
            nextMonday = nextMonday.plusWeeks(1);
        }

        return newWeeks;
    }
}