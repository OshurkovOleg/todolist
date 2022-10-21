package com.example.todolist.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "person_chat_id")
    private Long personChatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "language_code")
    private String languageCode;

    public Person(Long personChatId, String firstName, String lastName, String userName, String languageCode) {
        this.personChatId = personChatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.languageCode = languageCode;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Event> events;


    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", personChatId=" + personChatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", events=" + events +
                '}';
    }
}



