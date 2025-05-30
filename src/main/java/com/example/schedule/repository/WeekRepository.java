package com.example.schedule.repository;

import com.example.schedule.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week, Long> {
    boolean existsByWeekNumber(int weekNumber);
    Optional<Week> findByWeekNumber(int weekNumber);

    @Query("SELECT w FROM Week w WHERE w.startDate <= :date AND w.endDate >= :date")
    Optional<Week> findWeekContainingDate(@Param("date") LocalDate date);

    List<Week> findAllByOrderByWeekNumberAsc();

    @Query("SELECT w FROM Week w WHERE w.startDate > CURRENT_DATE ORDER BY w.startDate ASC")
    List<Week> findFutureWeeks();


    Optional<Week> findTopByOrderByWeekNumberDesc();
}
