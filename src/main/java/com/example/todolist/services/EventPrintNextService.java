package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.todolist.constants.Constants.*;

@Slf4j
@Service
public class EventPrintNextService {

    private final EventService eventService;

    @Autowired
    public EventPrintNextService(EventService eventService) {
        this.eventService = eventService;
    }

    public void print(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                      Integer commandType, Integer stepNumber) {
        Event event = eventService.find(eventService.getNextEvent());
        if (event != null) {
            sendMsg.accept(idChat, event.toString(), commandType, stepNumber);
        }
        log.info(NEXT_EVENT_FOUND);
    }
}
