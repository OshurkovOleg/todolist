package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.util.MessageUser;
import com.example.todolist.util.StepAndTypeCommandBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventDeleteService {
    public final String SPECIFY_EVENT_ID = "Укажите id события";
    public final String DELETE_EVENT_TEXT = "Событие удалено";
    EventService eventService;


    @Autowired
    public EventDeleteService(EventService eventService) {
        this.eventService = eventService;
    }

    public void delete(String idChat, int question, String text) {

        if (question == 0) {
            Bot.stepPosition++;
            MessageUser.send(idChat, SPECIFY_EVENT_ID);
        } else {
            StepAndTypeCommandBot.reset();
            eventService.deleteEvent(Long.parseLong(text));
            MessageUser.send(idChat, DELETE_EVENT_TEXT);
        }
    }


}
