package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import com.example.todolist.util.ParserStringToLocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


@Service
@Slf4j
public class EventAddService {

    private final String EVENT_CREATED_TEXT_LOG = "Событие создано";
    private final String NAME_TEXT = "Название";
    private final String DESCRIPTION_TEXT = "Описание";
    private final String PLACE_TEXT = "Место";
    private final String START_EVENT_TEXT = "Время и дата начала";
    private final String FINISH_EVENT_TEXT = "Время и дата конца";
    private final String NOTIFY_TEXT = "Оповещение в минутах";
    private final String SAVE_EVENT_TEXT = "Событие сохранено";
    private final EventService eventService;

    @Autowired
    public EventAddService(EventService eventService) {
        this.eventService = eventService;

    }

    public void add(String idChat, ArrayList<String> listUserAnswer,
                    FourthConsumer<String, String, Integer, Integer> sendMsg,
                    Integer commandType, Integer stepNumber) {

        if (stepNumber < 6) {
            Bot.stepNumber++;
        } else {
            long chatID = Long.parseLong(idChat);
            Event newEvent = getCompleteEvent(listUserAnswer, chatID);
            log.info(EVENT_CREATED_TEXT_LOG);
            eventService.save(newEvent);
            Bot.listUserAnswer.clear();
            sendMsg.accept(idChat, SAVE_EVENT_TEXT, commandType, stepNumber);
        }

        switch (stepNumber) {
            case 0 -> sendMsg.accept(idChat, NAME_TEXT, commandType, stepNumber);
            case 1 -> sendMsg.accept(idChat, DESCRIPTION_TEXT, commandType, stepNumber);
            case 2 -> sendMsg.accept(idChat, PLACE_TEXT, commandType, stepNumber);
            case 3 -> sendMsg.accept(idChat, START_EVENT_TEXT, commandType, stepNumber);
            case 4 -> sendMsg.accept(idChat, FINISH_EVENT_TEXT, commandType, stepNumber);
            case 5 -> sendMsg.accept(idChat, NOTIFY_TEXT, commandType, stepNumber);
        }
    }

    private Event getCompleteEvent(ArrayList<String> data, long chatID) {

        String name = data.get(0);
        String description = data.get(1);
        String place = data.get(2);

        LocalDateTime startEvent = ParserStringToLocalDate.parsing(data.get(3));
        LocalDateTime finishEvent = ParserStringToLocalDate.parsing(data.get(4));
        LocalDateTime currentDate = LocalDateTime.now();

        long eventDuration = startEvent.until(finishEvent, ChronoUnit.HOURS);
        int notify = Integer.parseInt(data.get(5));
        boolean notifyStatus = false;

        return new Event(name, description, place, startEvent, finishEvent, currentDate, currentDate,
                eventDuration, notify, notifyStatus, chatID);
    }

}
