package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.repository.EventRepository;
import com.example.todolist.util.MessageUser;
import com.example.todolist.util.StepAndTypeCommandBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    private static int editStep;
    private static int answer;
    private final EventService eventService;
    private final EventRepository eventRepository;


    @Autowired
    public EventEditService(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }


    public void edit(String idChat, int questionNumber, String text) {

//STEP 1
        if (questionNumber == 0) {

            Bot.stepPosition++;
            MessageUser.send(idChat, SPECIFY_EVENT_ID);

        }

//STEP 2
        if (questionNumber == 1) {

            Long idEvent = Long.parseLong(text);
            event = eventRepository.findById(idEvent).orElse(null);

            if (event != null && editStep == 0) {
                editStep = -1;
                Bot.stepPosition++;
                MessageUser.send(idChat, QUESTION_EDIT);

            } else {
                StepAndTypeCommandBot.reset();
                MessageUser.send(idChat, EVENT_MISSING_BY_ID);
            }

        }

//STEP 3
        if (text.equalsIgnoreCase(NAME_TEXT)) {
            MessageUser.send(idChat, NAME_TEXT_NEW);
            answer = 1;
        } else if (answer == 1) {
            event.setName(text);
            new EventService().save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(DESCRIPTION_TEXT)) {
            MessageUser.send(idChat, DESCRIPTION_TEXT_NEW);
            answer = 2;
        } else if (answer == 2) {
            event.setDescription(text);
            eventService.save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(PLACE_TEXT)) {
            MessageUser.send(idChat, PLACE_TEXT_NEW);
            answer = 3;
        } else if (answer == 3) {
            event.setPlace(text);
            eventService.save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(START_EVENT_TEXT)) {
            MessageUser.send(idChat, START_EVENT_TEXT_NEW);
            answer = 4;
        } else if (answer == 4) {
            event.setPlace(text);
            eventService.save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }

        if (text.equalsIgnoreCase(FINISH_EVENT_TEXT)) {
            MessageUser.send(idChat, FINISH_EVENT_TEXT_NEW);
            answer = 5;
        } else if (answer == 5) {
            event.setPlace(text);
            eventService.save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }


        if (text.equalsIgnoreCase(NOTIFY_TEXT)) {
            MessageUser.send(idChat, SAVE_EVENT_TEXT_NEW);
            answer = 6;
        } else if (answer == 6) {
            event.setPlace(text);
            eventService.save(event);
            editStep = 0;
            answer = 0;
            StepAndTypeCommandBot.reset();
            MessageUser.send(idChat, SUCCESSFUL_CHANGE);
        }
    }

}