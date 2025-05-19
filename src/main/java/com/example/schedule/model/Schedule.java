package com.example.schedule.model;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String subjectName;

    public Schedule(){}
    public Schedule(Week week, DayOfWeek dayOfWeek, String subjectName, LocalTime time) {
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.subjectName = subjectName;
        this.time = time;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Long getId(){
        return id;
    }

    public DayOfWeek getDayOfWeek(){
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek){
        this.dayOfWeek=dayOfWeek;
    }
    public String getSubjectName(){
        return subjectName;
    }

    public void setSubjectName(String subjectName){
        this.subjectName=subjectName;
    }


}
