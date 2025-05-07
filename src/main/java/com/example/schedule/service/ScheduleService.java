package com.example.schedule.service;


import com.example.schedule.model.DayOfWeek;
import com.example.schedule.model.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
                            scheduleRepository.save(new Schedule(day,"Матан"));
                            scheduleRepository.save(new Schedule(day,"Колебания волн каждый день"));
                        }
                    });

            System.out.println("инициализированы тестовые данные расписания");
        }
}

    //получить все записи
    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAllByOrderByDayOfWeekAsc();
    }
    // Предметы по дню недели
    public List<Schedule> getSchedulesByDay(DayOfWeek dayOfWeek){
        return scheduleRepository.findByDayOfWeekOrderByIdAsc(dayOfWeek);
    }
    //add subject
    public Schedule addSubject(DayOfWeek dayOfWeek , String subjectName){
        if (scheduleRepository.existsByDayOfWeekAndSubjectName(dayOfWeek, subjectName)) {
            throw  new RuntimeException("Этот предмет уже есть в расписании на  "+dayOfWeek.getDisplayName());
        }
        return scheduleRepository.save(new Schedule(dayOfWeek, subjectName));

    };

    public void deleteSubject(long id){
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }
}
