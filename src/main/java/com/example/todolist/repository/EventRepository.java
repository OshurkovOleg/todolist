package com.example.todolist.repository;

import com.example.todolist.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface EventRepository extends CrudRepository<Event, Long> {

}


