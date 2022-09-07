package com.example.todolist.services;

import com.example.todolist.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class EventDeleteService {
    public final String SPECIFY_EVENT_ID = "Укажите id события";
    public final String DELETE_EVENT_TEXT = "Событие удалено";
    private final EventService eventService;


    @Autowired
    public EventDeleteService(EventService eventService) {
        this.eventService = eventService;
    }

    public void delete(String idChat, int question, String text, BiConsumer<String, String> test) {

        if (question == 0) {
            Bot.stepPosition++;
            test.accept(idChat, SPECIFY_EVENT_ID);
        } else {
            eventService.deleteEvent(Long.parseLong(text));
            test.accept(idChat, DELETE_EVENT_TEXT);
        }
    }


}
