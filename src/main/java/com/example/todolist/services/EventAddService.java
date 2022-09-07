package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.util.ParserStringToLocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.BiConsumer;


@Service
@Slf4j
public class EventAddService {

    public final String EVENT_CREATED_TEXT_LOG = "Событие создано";
    public final String NAME_TEXT = "Название";
    public final String DESCRIPTION_TEXT = "Описание";
    public final String PLACE_TEXT = "Место";
    public final String START_EVENT_TEXT = "Время и дата начала";
    public final String FINISH_EVENT_TEXT = "Время и дата конца";
    public final String NOTIFY_TEXT = "Оповещение в минутах";
    public final String SAVE_EVENT_TEXT = "Событие сохранено";
    private final EventService eventService;

    @Autowired
    public EventAddService(EventService eventService) {
        this.eventService = eventService;

    }

    public void add(String idChat, int step, ArrayList<String> listUserAnswer, BiConsumer<String, String> test) {

        if (step < 6) {
            Bot.stepPosition++;
        } else {
            Event newEvent = getCompleteEvent(listUserAnswer);
            log.info(EVENT_CREATED_TEXT_LOG);
            eventService.save(newEvent);
            Bot.listUserAnswer = new ArrayList<>();
            test.accept(idChat, SAVE_EVENT_TEXT);
        }

        switch (step) {
            case 0 -> test.accept(idChat, NAME_TEXT);
            case 1 -> test.accept(idChat, DESCRIPTION_TEXT);
            case 2 -> test.accept(idChat, PLACE_TEXT);
            case 3 -> test.accept(idChat, START_EVENT_TEXT);
            case 4 -> test.accept(idChat, FINISH_EVENT_TEXT);
            case 5 -> test.accept(idChat, NOTIFY_TEXT);
        }
    }

    private Event getCompleteEvent(ArrayList<String> data) {

        String name = data.get(0);
        String description = data.get(1);
        String place = data.get(2);

        LocalDateTime startEvent = ParserStringToLocalDate.parsing(data.get(3));
        LocalDateTime finishEvent = ParserStringToLocalDate.parsing(data.get(4));
        LocalDateTime currentDate = LocalDateTime.now();
        long eventDuration = startEvent.until(finishEvent, ChronoUnit.HOURS);
        int notify = Integer.parseInt(data.get(5));
        boolean notifyStatus = false;

        return new Event(name, description, place, startEvent, finishEvent, currentDate, currentDate, eventDuration, notify, notifyStatus);
    }

}
