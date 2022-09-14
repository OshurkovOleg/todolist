package com.example.todolist.services;

import com.example.todolist.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPrintNextService {
    private EventService eventService;

    @Autowired
    public EventPrintNextService(EventService eventService) {
        this.eventService = eventService;
    }

    public void print(String idChat, FourthConsumer<String, String, Integer, Integer> sendMsg,
                      Integer commandType, Integer stepNumber) {
        Event event = eventService.find(eventService.getNextEvent());
        sendMsg.accept(idChat, event.toString(), commandType, stepNumber);
    }
}
