package com.example.todolist.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
@Data
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_chat_id")
    private Long personChatId;

    @OneToMany(mappedBy = "person")
    private List<Event> events;

    public Person() {
    }

    public Person(Long personChatId, Event event) {
        this.personChatId = personChatId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personName='" + personName + '\'' +
                ", personChatId=" + personChatId +
                '}';
    }
}



