package com.example.todolist.services;

import com.example.todolist.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Service
public class EventPrintAllService {

    private final EventService eventService;

    public EventPrintAllService(EventService eventService) {
        this.eventService = eventService;
    }

    public void printAll(String idChat, BiConsumer<String, String> test) {
        List<Event> eventList = eventService.getAllEventAsListFromBase();

        for (Event event : eventList) {
            test.accept(idChat, event.toString());
        }
    }


}
