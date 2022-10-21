package com.example.todolist.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "history")
public class History {
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
    @Column(name = "notify_event")
    private int notifyBeforeEventHours;
    @Column(name = "notify_status")
    private boolean notifyStatus;
    @Column(name = "chat_id")
    private long chatID;

    public History() {
    }

    public History(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.place = event.getPlace();
        this.startExecution = event.getStartExecution();
        this.endExecution = event.getEndExecution();
        this.dateCreation = event.getDateCreation();
        this.lastUpdate = event.getLastUpdate();
        this.duration = event.getDuration();
        this.notifyBeforeEventHours = event.getNotifyBeforeEventHours();
        this.notifyStatus = event.isNotifyStatus();
        this.chatID = event.getChatID();
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
