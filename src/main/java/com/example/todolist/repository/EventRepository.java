package com.example.todolist.repository;

import com.example.todolist.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository()
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByDateCreationBetween(Date from, Date to);






}


