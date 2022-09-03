package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.util.MessageUser;
import com.example.todolist.util.StepAndTypeCommandBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPrintAllService {

    EventService eventService;

    @Autowired
    public EventPrintAllService(EventService eventService) {
        this.eventService = eventService;
    }

    public void printAll(String idChat) {
        StepAndTypeCommandBot.reset();
        for (Event event : eventService.printAll()) {
            MessageUser.send(idChat, event.toString());
        }
    }


}
