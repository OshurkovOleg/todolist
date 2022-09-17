package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class EventPrintAllService {

    private final EventService eventService;

    @Autowired
    public EventPrintAllService(EventService eventService) {
        this.eventService = eventService;
    }

    public void printAll(String idChat, FourthConsumer<String, String, Integer, Integer> sendMsg,
                         Integer commandType, Integer stepNumber) {

        eventService.getAllEventAsListFromBase().stream()
                .sorted(Comparator.comparing(Event::getStartExecution))
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));

    }


}
