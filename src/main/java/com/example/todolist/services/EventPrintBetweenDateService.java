package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.util.FourthConsumer;
import com.example.todolist.util.ParserStringToLocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.todolist.constants.Constants.*;

@Slf4j
@Service
public class EventPrintBetweenDateService {
    private final EventService eventService;

    @Autowired
    public EventPrintBetweenDateService(EventService eventService) {
        this.eventService = eventService;
    }

    public void print(Long idChat, ArrayList<String> listAnswer, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                      Integer commandType, Integer stepNumber) {


        if (stepNumber == 0) {
            getStartDateFromUser(idChat, sendMsg, commandType, stepNumber);
        } else if (stepNumber == 1) {
            getEndDateFromUser(idChat, sendMsg, commandType, stepNumber);
        } else if (stepNumber == 2) {
            printEventsPerPeriod(idChat, listAnswer, sendMsg, commandType, stepNumber);
        }
    }

    private static void getStartDateFromUser(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        Bot.stepNumber++;
        sendMsg.accept(idChat, START_DATE, commandType, stepNumber);
    }

    private static void getEndDateFromUser(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        Bot.stepNumber++;
        sendMsg.accept(idChat, END_DATE, commandType, stepNumber);
    }

    private void printEventsPerPeriod(Long idChat, ArrayList<String> listAnswer, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        LocalDateTime fromDate = ParserStringToLocalDate.parsing(listAnswer.get(0));
        LocalDateTime byDate = ParserStringToLocalDate.parsing(listAnswer.get(1));

        eventService.getBetweenDatesListEvents(fromDate, byDate)
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));
        log.info(SELECTION_EVENTS_MADE);
        Bot.listUserAnswer.clear();
    }


}
