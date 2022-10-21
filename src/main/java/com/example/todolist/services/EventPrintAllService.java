package com.example.todolist.services;

import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.example.todolist.constants.Constants.*;

@Slf4j
@Service
public class EventPrintAllService {
    private final PersonService personService;

    @Autowired
    public EventPrintAllService(PersonService personService) {
        this.personService = personService;
    }

    public void printAll(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                         Integer commandType, Integer stepNumber) {

        personService.getByChatId(idChat).getEvents().stream()
                .sorted(Comparator.comparing((Event::getIdEvent)))
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));
        log.info(SELECTION_EVENTS_MADE);

    }
}
