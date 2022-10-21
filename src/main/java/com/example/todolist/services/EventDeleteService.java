package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.util.FourthConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.todolist.constants.Constants.DELETE_EVENT_TEXT;
import static com.example.todolist.constants.Constants.SPECIFY_EVENT_ID;

import static com.example.todolist.constants.Constants.*;
@Slf4j
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
            log.info(EVENT_DELETED_FROM_BASE);
            sendMsg.accept(idChat, DELETE_EVENT_TEXT, commandType, stepNumber);

        }
    }


}
