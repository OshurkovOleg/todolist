package com.example.todolist.services;

import com.example.todolist.Bot;
import com.example.todolist.model.Event;
import com.example.todolist.util.FourthConsumer;
import com.example.todolist.util.ParserStringToLocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.todolist.constants.Constants.*;

@Slf4j
@Service
public class EventEditService {
    private static Event event;
    private static boolean routeStep;
    private static int userAnswer;
    private final EventService eventService;

    @Autowired
    public EventEditService(EventService eventService) {
        this.eventService = eventService;
    }

    public void edit(Long idChat, String textMsg, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                     Integer commandType, Integer stepNumber) {

        if (stepNumber == 0) {
            getIdForEdit(idChat, sendMsg, commandType, stepNumber);
        }

        if (stepNumber == 1) {
            getFieldForEdit(idChat, textMsg, sendMsg, commandType, stepNumber);
        }

        if(stepNumber == 2) {
            setNewValueForEvent(idChat, textMsg, sendMsg, commandType, stepNumber);
        }

    }

    private static void getIdForEdit(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        Bot.stepNumber++;
        sendMsg.accept(idChat, SPECIFY_EVENT_ID, commandType, stepNumber);
    }

    private void getFieldForEdit(Long idChat, String textMsg, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        Long idEvent = Long.parseLong(textMsg);
        event = eventService.find(idEvent);
        log.info(EVENT_FOUND_IN_BASE);

        if (event.getChatID() == idChat && event != null && !routeStep) {
            routeStep = true;
            Bot.stepNumber++;
            sendMsg.accept(idChat, QUESTION_EDIT, commandType, stepNumber);
        } else {
            sendMsg.accept(idChat, EVENT_MISSING_BY_ID, commandType, stepNumber);
        }
    }

    private void setNewValueForEvent(Long idChat, String textMsg, FourthConsumer<Long, String, Integer, Integer> sendMsg, Integer commandType, Integer stepNumber) {
        if (textMsg.equalsIgnoreCase(NAME_TEXT)) {
            sendMsg.accept(idChat, NAME_TEXT_NEW, commandType, stepNumber);
            userAnswer = 1;
        } else if (userAnswer == 1) {
            event.setName(textMsg);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(DESCRIPTION_TEXT)) {
            sendMsg.accept(idChat, DESCRIPTION_TEXT_NEW, commandType, stepNumber);
            userAnswer = 2;
        } else if (userAnswer == 2) {
            event.setDescription(textMsg);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(PLACE_TEXT)) {
            sendMsg.accept(idChat, PLACE_TEXT_NEW, commandType, stepNumber);
            userAnswer = 3;
        } else if (userAnswer == 3) {
            event.setPlace(textMsg);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(START_EVENT_TEXT)) {
            sendMsg.accept(idChat, START_EVENT_TEXT_NEW, commandType, stepNumber);
            userAnswer = 4;
        } else if (userAnswer == 4) {
            LocalDateTime startDate = ParserStringToLocalDate.parsing(textMsg);
            event.setStartExecution(startDate);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }

        if (textMsg.equalsIgnoreCase(FINISH_EVENT_TEXT)) {
            sendMsg.accept(idChat, FINISH_EVENT_TEXT_NEW, commandType, stepNumber);
            userAnswer = 5;
        } else if (userAnswer == 5) {
            LocalDateTime endDate = ParserStringToLocalDate.parsing(textMsg);
            event.setEndExecution(endDate);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }


        if (textMsg.equalsIgnoreCase(NOTIFY_TEXT)) {
            sendMsg.accept(idChat, SAVE_EVENT_TEXT_NEW, commandType, stepNumber);
            userAnswer = 6;
        } else if (userAnswer == 6) {
            int timeNotify = Integer.parseInt(textMsg);
            event.setNotifyBeforeEventHours(timeNotify);
            eventService.save(event);
            routeStep = false;
            userAnswer = 0;
            sendMsg.accept(idChat, SUCCESSFUL_CHANGE, commandType, stepNumber);
        }
    }

}