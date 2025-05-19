package com.example.schedule.service;


import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository=scheduleRepository;
    }
    //инициализация начальных данных
    @PostConstruct
    public void init(){
        if (scheduleRepository.count()==0){
            Arrays.stream(DayOfWeek.values())
                    .forEach(day ->{
                        if (day!=DayOfWeek.SATURDAY && day!=DayOfWeek.SUNDAY){
                            scheduleRepository.save(new Schedule(day,"Матан",LocalTime.of(9,0)));
                            scheduleRepository.save(new Schedule(day,"Колебания волн каждый день",LocalTime.of(11,0)));
                        }
                    });

            System.out.println("инициализированы тестовые данные расписания");
        }
    }

    //получить все записи
    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAllByOrderByDayOfWeekAscTimeAsc();
    }
    // Предметы по дню недели
    public List<Schedule> getSchedulesByDay(DayOfWeek dayOfWeek){
        return scheduleRepository.findByDayOfWeekOrderByTimeAsc(dayOfWeek);
    }
    //add subject
    public Schedule addSubject(DayOfWeek dayOfWeek , String subjectName, LocalTime time){

        if (time.isBefore(LocalTime.of(8, 0))) {
            throw new IllegalArgumentException("Время слишком раннее (минимум 8:00)");
        }
        if (time.isAfter(LocalTime.of(20, 0))) {
            throw new IllegalArgumentException("Время слишком позднее (максимум 20:00)");
        }
        List<Schedule> existingSubjects = scheduleRepository.findByDayOfWeekOrderByTimeAsc(dayOfWeek);
        for (Schedule existing : existingSubjects) {
            long diffMinutes = Math.abs(time.toSecondOfDay() - existing.getTime().toSecondOfDay()) / 60;
            if (diffMinutes < 60) {
                throw new IllegalArgumentException(
                        "Интервал между предметами должен быть ≥ 1 часа! " +
                                "Конфликт с предметом: " + existing.getSubjectName() + " (" + existing.getTime() + ")"
                );
            }
        }

        //тут был блокировщик
        return scheduleRepository.save(new Schedule(dayOfWeek, subjectName,time));

    };


    public void deleteSubject(long id){
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }
}
