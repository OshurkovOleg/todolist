package com.example.todolist.repository;

import com.example.todolist.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository()
public interface EventRepository extends CrudRepository<Event, Long> {
    Event findFirstByStartExecutionAfterOrderByEndExecution(LocalDateTime dateCreation);

    List<Event> findAllByStartExecutionBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findAllByStartExecutionAfter(LocalDateTime nowLocalDate);
}


