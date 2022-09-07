package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;


@Service
public class EventEditService {
    public final String SPECIFY_EVENT_ID = "Укажите id события";
    public final String SUCCESSFUL_CHANGE = "Изменение успешно внесено";
    public final String SAVE_EVENT_TEXT_NEW = "Укажите новое оповещение в минутах";
    public final String FINISH_EVENT_TEXT_NEW = "Укажите новые время и дату конца";
    public final String START_EVENT_TEXT_NEW = "Укажите новые время и дату начала";
    public final String PLACE_TEXT_NEW = "Новое место";
    public final String DESCRIPTION_TEXT_NEW = "Новое описание";
    public final String NAME_TEXT_NEW = "Новое название";
    public final String EVENT_MISSING_BY_ID = "Событие с указанным id отсутствует";
    public final String QUESTION_EDIT = "Что редактируем?";
    public final String NAME_TEXT = "Название";
    public final String DESCRIPTION_TEXT = "Описание";
    public final String PLACE_TEXT = "Место";
    public final String START_EVENT_TEXT = "Время и дата начала";
    public final String FINISH_EVENT_TEXT = "Время и дата конца";
    public final String NOTIFY_TEXT = "Оповещение в минутах";
    private static Event event;
    private static boolean routeStep;
    private static int answer;
    private final EventService eventService;

    @Autowired
    public EventEditService(EventService eventService) {
        this.eventService = eventService;
    }

    public void edit(String idChat, int questionNumber, String text, BiConsumer<String, String> sendMsg) {

//STEP 1
        if (questionNumber == 0) {

            Bot.stepPosition++;
            sendMsg.accept(idChat, SPECIFY_EVENT_ID);

        }

//STEP 2
        if (questionNumber == 1) {

            Long idEvent = Long.parseLong(text);
            event = eventService.find(idEvent);

            if (event != null && !routeStep) {
                routeStep = true;
                Bot.stepPosition++;
                sendMsg.accept(idChat, QUESTION_EDIT);

            } else {
                sendMsg.accept(idChat, EVENT_MISSING_BY_ID);
            }

        }

//STEP 3
        if (text.equalsIgnoreCase(NAME_TEXT)) {
            sendMsg.accept(idChat, NAME_TEXT_NEW);
            answer = 1;
        } else if (answer == 1) {
            event.setName(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(DESCRIPTION_TEXT)) {
            sendMsg.accept(idChat, DESCRIPTION_TEXT_NEW);
            answer = 2;
        } else if (answer == 2) {
            event.setDescription(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(PLACE_TEXT)) {
            sendMsg.accept(idChat, PLACE_TEXT_NEW);
            answer = 3;
        } else if (answer == 3) {
            event.setPlace(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(START_EVENT_TEXT)) {
            sendMsg.accept(idChat, START_EVENT_TEXT_NEW);
            answer = 4;
        } else if (answer == 4) {
            event.setPlace(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(FINISH_EVENT_TEXT)) {
            sendMsg.accept(idChat, FINISH_EVENT_TEXT_NEW);
            answer = 5;
        } else if (answer == 5) {
            event.setPlace(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }


        if (text.equalsIgnoreCase(NOTIFY_TEXT)) {
            sendMsg.accept(idChat, SAVE_EVENT_TEXT_NEW);
            answer = 6;
        } else if (answer == 6) {
            event.setPlace(text);
            eventService.save(event);
            routeStep = false;
            answer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE);
        }
    }

}