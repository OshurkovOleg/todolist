package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventService {
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public List<Event> getAllEventAsListFromBase() {
        List<Event> eventsList = new ArrayList<>();
        eventRepository.findAll().forEach(eventsList::add);
        return eventsList;
    }

    public Event find(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Long getNextEvent() {
        try {
            return eventRepository.findFirstByStartExecutionAfterOrderByEndExecution(LocalDateTime.now()).getId();
        } catch (NullPointerException e) {
            return 0L;
        }
    }

    public List<Event> getBetweenDatesListEvents(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findAllByStartExecutionBetween(start, end);
    }

    public List<Event> getAllEventIsAfterDateNow() {
        return eventRepository.findAllByStartExecutionAfter(LocalDateTime.now());
    }


}
