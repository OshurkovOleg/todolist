package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventService() {
    }

    public Optional<Event> findByIdEvent(Long id) {
        return eventRepository.findById(id);
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


}
