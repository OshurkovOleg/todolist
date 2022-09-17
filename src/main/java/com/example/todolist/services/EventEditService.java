package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import com.example.todolist.util.ParserStringToLocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class EventEditService {
    private final String SPECIFY_EVENT_ID = "Укажите id события";
    private final String SUCCESSFUL_CHANGE = "Изменение успешно внесено";
    private final String SAVE_EVENT_TEXT_NEW = "Укажите новое оповещение в минутах";
    private final String FINISH_EVENT_TEXT_NEW = "Укажите новые время и дату конца";
    private final String START_EVENT_TEXT_NEW = "Укажите новые время и дату начала";
    private final String PLACE_TEXT_NEW = "Новое место";
    private final String DESCRIPTION_TEXT_NEW = "Новое описание";
    private final String NAME_TEXT_NEW = "Новое название";
    private final String EVENT_MISSING_BY_ID = "Событие с указанным id отсутствует";
    private final String QUESTION_EDIT = "Что редактируем?";
    private final String NAME_TEXT = "Название";
    private final String DESCRIPTION_TEXT = "Описание";
    private final String PLACE_TEXT = "Место";
    private final String START_EVENT_TEXT = "Время и дата начала";
    private final String FINISH_EVENT_TEXT = "Время и дата конца";
    private final String NOTIFY_TEXT = "Оповещение в минутах";
    private static Event event;
    private static boolean routeStep;
    private static int answer;
    private final EventService eventService;

    @Autowired
    public EventEditService(EventService eventService) {
        this.eventService = eventService;
    }

    public void edit(String idChat, String textMsg, FourthConsumer<String, String, Integer, Integer> sendMsg,
                     Integer commandType, Integer stepNumber) {

//STEP 1
        if (stepNumber == 0) {

            Bot.stepNumber++;
            sendMsg.accept(idChat, SPECIFY_EVENT_ID, commandType, stepNumber);

        }

//STEP 2
        if (stepNumber == 1) {

            Long idEvent = Long.parseLong(textMsg);
            event = eventService.find(idEvent);

            if (event != null && !routeStep) {
                routeStep = true;
                Bot.stepNumber++;
                sendMsg.accept(idChat, QUESTION_EDIT, commandType, stepNumber);

            } else {
                sendMsg.accept(idChat, EVENT_MISSING_BY_ID, commandType, stepNumber);
            }

        }

//STEP 3
        if (textMsg.equalsIgnoreCase(NAME_TEXT)) {
            sendMsg.accept(idChat, NAME_TEXT_NEW, commandType, stepNumber);
            answer = 1;
        } else if (answer == 1) {
            event.setName(textMsg);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(DESCRIPTION_TEXT)) {
            sendMsg.accept(idChat, DESCRIPTION_TEXT_NEW, commandType, stepNumber);
            answer = 2;
        } else if (answer == 2) {
            event.setDescription(textMsg);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(PLACE_TEXT)) {
            sendMsg.accept(idChat, PLACE_TEXT_NEW, commandType, stepNumber);
            answer = 3;
        } else if (answer == 3) {
            event.setPlace(textMsg);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(START_EVENT_TEXT)) {
            sendMsg.accept(idChat, START_EVENT_TEXT_NEW, commandType, stepNumber);
            answer = 4;
        } else if (answer == 4) {
            LocalDateTime startDate = ParserStringToLocalDate.parsing(textMsg);
            event.setStartExecution(startDate);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(FINISH_EVENT_TEXT)) {
            sendMsg.accept(idChat, FINISH_EVENT_TEXT_NEW, commandType, stepNumber);
            answer = 5;
        } else if (answer == 5) {
            LocalDateTime endDate = ParserStringToLocalDate.parsing(textMsg);
            event.setEndExecution(endDate);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }


        if (textMsg.equalsIgnoreCase(NOTIFY_TEXT)) {
            sendMsg.accept(idChat, SAVE_EVENT_TEXT_NEW, commandType, stepNumber);
            answer = 6;
        } else if (answer == 6) {
            int timeNotify = Integer.parseInt(textMsg);
            event.setNotifyBeforeEventHours(timeNotify);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }


    }

}