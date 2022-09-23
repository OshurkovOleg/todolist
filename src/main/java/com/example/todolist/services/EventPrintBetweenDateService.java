package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import com.example.todolist.util.ParserStringToLocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.todolist.constants.Constants.*;

@Component
public class EventPrintBetweenDateService {
    private final EventService eventService;

    @Autowired
    public EventPrintBetweenDateService(EventService eventService) {
        this.eventService = eventService;
    }

    public void print(Long idChat, ArrayList<String> listAnswer, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                      Integer commandType, Integer stepNumber) {

        LocalDateTime start;
        LocalDateTime end;


        if (stepNumber == 0) {
            Bot.stepNumber++;
            sendMsg.accept(idChat, START_DATE, commandType, stepNumber);
        }

        if (stepNumber == 1) {
            Bot.stepNumber++;
            sendMsg.accept(idChat, END_DATE, commandType, stepNumber);
        }

        if (stepNumber == 2) {
            start = ParserStringToLocalDate.parsing(listAnswer.get(0));
            end = ParserStringToLocalDate.parsing(listAnswer.get(1));


            for (Event event : eventService.getBetweenDatesListEvents(start, end)) {
                sendMsg.accept(idChat, event.toString(), commandType, stepNumber);
            }

            Bot.listUserAnswer.clear();
        }


    }
}
