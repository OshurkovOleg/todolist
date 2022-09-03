package com.example.todolist;

import com.example.todolist.services.EventAddService;
import com.example.todolist.services.EventDeleteService;
import com.example.todolist.services.EventEditService;
import com.example.todolist.services.EventPrintAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Component
public class Bot extends TelegramLongPollingBot {
    private final String name = "EventLogBot";
    private final String token = "5610627690:AAHBq3KzQr2WUKU5oOMCDu9nfHQzhPJAoTA";
    private static EventEditService eventEditService;
    private static EventAddService eventAddService;
    private static EventDeleteService eventDeleteService;
    private static EventPrintAllService eventPrintAllService;
    public static ArrayList<String> listUserAnswer = new ArrayList<>();
    public static int typeCommand = 0;
    public static int stepPosition = 0;

    @Autowired
    public Bot(EventEditService eventEditService, EventAddService eventAddService,
               EventDeleteService eventDeleteService, EventPrintAllService eventPrintAllService) {
        Bot.eventEditService = eventEditService;
        Bot.eventAddService = eventAddService;
        Bot.eventDeleteService = eventDeleteService;
        Bot.eventPrintAllService = eventPrintAllService;
    }

    public Bot() {
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        String chatIdUser = update.getMessage().getChatId().toString();
        String textMessage = update.getMessage().getText();

        if (textMessage.contains("/")) {

            if (textMessage.contains("/add")) {
                stepPosition = 0;
                typeCommand = 1;
                eventAddService.add(chatIdUser, stepPosition, listUserAnswer);
            }

            if (textMessage.contains("/delete")) {
                stepPosition = 0;
                typeCommand = 2;
                eventDeleteService.delete(chatIdUser, stepPosition, textMessage);
            }

            if (textMessage.contains("/edit")) {
                stepPosition = 0;
                typeCommand = 3;
                eventEditService.edit(chatIdUser, stepPosition, textMessage);
            }

            if (textMessage.contains("/print")) {
                eventPrintAllService.printAll(chatIdUser);
            }


        } else {

            if (typeCommand == 1) {
                listUserAnswer.add(textMessage);
                eventAddService.add(chatIdUser, stepPosition, listUserAnswer);
            }

            if (typeCommand == 2) {
                eventDeleteService.delete(chatIdUser, stepPosition, textMessage);
            }

            if (typeCommand == 3) {
                eventEditService.edit(chatIdUser, stepPosition, textMessage);
            }

        }
    }
}
