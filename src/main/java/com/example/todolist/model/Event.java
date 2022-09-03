package com.example.todolist.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "place")
    private String place;
    @Column(name = "start_execution")
    private LocalDateTime startExecution;
    @Column(name = "end_execution")
    private LocalDateTime endExecution;
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    @Column(name = "duration")
    private long duration;
    @Column(name = "notify_before_event_hours")
    private int notifyBeforeEventHours;

    public Event() {
    }

    public Event(String name, String description, String place, LocalDateTime startExecution, LocalDateTime endExecution, LocalDateTime dateCreation, LocalDateTime lastUpdate, long duration, int notifyBeforeEventHours) {
        this.name = name;
        this.description = description;
        this.place = place;
        this.startExecution = startExecution;
        this.endExecution = endExecution;
        this.dateCreation = dateCreation;
        this.lastUpdate = lastUpdate;
        this.duration = duration;
        this.notifyBeforeEventHours = notifyBeforeEventHours;
    }

    @Override
    public String toString() {
        return "id = " + id + "\n"
                + "Название = " + name + "\n"
                + "Описание = " + description + "\n"
                + "Место = " + place + "\n"
                + "Начало события = " + startExecution + "\n"
                + "Конец события = " + endExecution + "\n";
    }
}
