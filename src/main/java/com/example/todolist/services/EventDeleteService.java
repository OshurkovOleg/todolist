package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.util.FourthConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventDeleteService {
    private final String SPECIFY_EVENT_ID = "Укажите id события";
    private final String DELETE_EVENT_TEXT = "Событие удалено";
    private final EventService eventService;


    @Autowired
    public EventDeleteService(EventService eventService) {
        this.eventService = eventService;
    }

    public void delete(String idChat, String textMsg, FourthConsumer<String, String, Integer, Integer> sendMsg,
                       Integer commandType, Integer stepNumber) {

        if (stepNumber == 0) {
            Bot.stepNumber++;
            sendMsg.accept(idChat, SPECIFY_EVENT_ID, commandType, stepNumber);
        } else {
            eventService.deleteEvent(Long.parseLong(textMsg));
            sendMsg.accept(idChat, DELETE_EVENT_TEXT, commandType, stepNumber);
        }
    }


}
