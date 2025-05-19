package com.example.schedule.repository;

import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByDayOfWeekOrderByIdAsc(DayOfWeek dayOfWeek);
    List<Schedule> findAllByOrderByDayOfWeekAsc();
    List<Schedule> findByDayOfWeekOrderByTimeAsc(DayOfWeek dayOfWeek);
    List<Schedule> findAllByOrderByDayOfWeekAscTimeAsc();
    boolean existsByDayOfWeekAndSubjectName(DayOfWeek dayOfWeek, String subjectName);
}
