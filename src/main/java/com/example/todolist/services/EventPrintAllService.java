package com.example.todolist.services;

import com.example.todolist.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventPrintAllService {

    private final EventService eventService;

    @Autowired
    public EventPrintAllService(EventService eventService) {
        this.eventService = eventService;
    }

    public void printAll(String idChat, FourthConsumer<String, String, Integer, Integer> sendMsg,
                         Integer commandType, Integer stepNumber) {
        List<Event> eventList = eventService.getAllEventAsListFromBase();

        for (Event event : eventList) {
            sendMsg.accept(idChat, event.toString(), commandType, stepNumber);
        }
    }


}
