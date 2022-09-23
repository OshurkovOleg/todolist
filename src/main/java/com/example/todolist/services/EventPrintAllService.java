package com.example.todolist.services;

import com.example.todolist.util.FourthConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPrintAllService {

    private final EventService eventService;

    @Autowired
    public EventPrintAllService(EventService eventService) {
        this.eventService = eventService;
    }

    public void printAll(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                         Integer commandType, Integer stepNumber) {

    /*    eventService.getAllEventAsListFromBase().stream()
                .sorted(Comparator.comparing(Event::getStartExecution))
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));*/

        eventService.getAllEventAsListFromBase().stream()
                .filter(event -> event.getChatID() == idChat)
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));


    }


}
