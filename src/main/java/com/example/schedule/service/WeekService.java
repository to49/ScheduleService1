package com.example.schedule.service;

import com.example.schedule.model.Week;
import com.example.schedule.repository.WeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
public class WeekService {
    @Autowired
    private WeekRepository weekRepository;

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
}