package com.example.todolist.repository;

import com.example.todolist.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findFirstByPersonChatId(Long chatId);
}
