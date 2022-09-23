package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.util.FourthConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.todolist.constants.Constants.DELETE_EVENT_TEXT;
import static com.example.todolist.constants.Constants.SPECIFY_EVENT_ID;

@Service
public class EventDeleteService {
    private final EventService eventService;

    @Autowired
    public EventDeleteService(EventService eventService) {
        this.eventService = eventService;
    }

    public void delete(Long idChat, String textMsg, FourthConsumer<Long, String, Integer, Integer> sendMsg,
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
