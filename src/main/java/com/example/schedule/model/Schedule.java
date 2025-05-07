package com.example.schedule.model;
import jakarta.persistence.*;
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;
    @Column(nullable = false)
    private String subjectName;

    public Schedule(){}
    public Schedule(DayOfWeek dayOfWeek , String subjectName) {
        this.dayOfWeek=dayOfWeek;
        this.subjectName=subjectName;
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
