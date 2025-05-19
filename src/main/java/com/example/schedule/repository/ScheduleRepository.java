package com.example.schedule.repository;

import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByWeekAndDayOfWeekOrderByTimeAsc(Week week, DayOfWeek dayOfWeek);
    List<Schedule> findByWeekOrderByDayOfWeekAscTimeAsc(Week week);
    boolean existsByWeekAndDayOfWeekAndTime(Week week, DayOfWeek dayOfWeek, LocalTime time);
}
