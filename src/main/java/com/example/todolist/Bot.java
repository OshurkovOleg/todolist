package com.example.todolist;

import com.example.todolist.model.Event;
import com.example.todolist.services.*;
import com.example.todolist.util.StepAndTypeCommandBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    private static final String FIELD_NAME = "Название";
    private static final String FIELD_DESCRIPTION = "Описание";
    private static final String FIELD_PLACE = "Место";
    private static final String FIELD_NOTIFY = "Оповещение в минутах";
    private final String name = "EventLogBot";
    private final String token = "5610627690:AAHBq3KzQr2WUKU5oOMCDu9nfHQzhPJAoTA";
    private EventEditService eventEditService;
    private EventAddService eventAddService;
    private EventDeleteService eventDeleteService;
    private EventPrintAllService eventPrintAllService;
    private EventPrintNextService eventPrintNextService;
    private EventPrintBetweenDateService eventPrintBetweenDateService;
    private EventService eventService;
    public static ArrayList<String> listUserAnswer = new ArrayList<>();
    public static Integer commandType = 0;
    public static int stepNumber = 0;

    @Autowired
    private Bot(EventEditService eventEditService, EventAddService eventAddService,
               EventDeleteService eventDeleteService, EventPrintAllService eventPrintAllService,
               EventPrintNextService eventPrintNextService, EventService eventService,
               EventPrintBetweenDateService eventPrintBetweenDateService) {

        this.eventEditService = eventEditService;
        this.eventAddService = eventAddService;
        this.eventDeleteService = eventDeleteService;
        this.eventPrintAllService = eventPrintAllService;
        this.eventPrintNextService = eventPrintNextService;
        this.eventService = eventService;
        this.eventPrintBetweenDateService = eventPrintBetweenDateService;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Bot started");
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }

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

            if (textMessage.equals("/add")) {
                StepAndTypeCommandBot.reset();
                commandType = 1;
                eventAddService.add(chatIdUser, listUserAnswer, this::send, commandType, stepNumber);
            }

            if (textMessage.equals("/delete")) {
                StepAndTypeCommandBot.reset();
                commandType = 2;
                eventDeleteService.delete(chatIdUser, textMessage, this::send, commandType, stepNumber);
            }

            if (textMessage.equals("/edit")) {
                StepAndTypeCommandBot.reset();
                commandType = 3;
                eventEditService.edit(chatIdUser, textMessage, this::send, commandType, stepNumber);
            }

            if (textMessage.equals("/print")) {
                StepAndTypeCommandBot.reset();
                commandType = 4;
                eventPrintAllService.printAll(chatIdUser, this::send, commandType, stepNumber);
            }

            if (textMessage.equals("/print_next_event")) {
                StepAndTypeCommandBot.reset();
                commandType = 5;
                eventPrintNextService.print(chatIdUser, this::send, commandType, stepNumber);
            }

            if (textMessage.equals("/print_event_by_date")) {
                StepAndTypeCommandBot.reset();
                commandType = 6;
                eventPrintBetweenDateService.print(chatIdUser, listUserAnswer, this::send, commandType, stepNumber);


            }


        } else {

            if (commandType == 1) {
                listUserAnswer.add(textMessage);
                eventAddService.add(chatIdUser, listUserAnswer, this::send, commandType, stepNumber);
            }

            if (commandType == 2) {
                eventDeleteService.delete(chatIdUser, textMessage, this::send, commandType, stepNumber);
            }

            if (commandType == 3) {
                eventEditService.edit(chatIdUser, textMessage, this::send, commandType, stepNumber);
            }

            if (commandType == 6) {
                listUserAnswer.add(textMessage);
                eventPrintBetweenDateService.print(chatIdUser, listUserAnswer, this::send, commandType, stepNumber);
            }
        }
    }

    final SendMessage sendMessage = new SendMessage();

    private void send(String idChatUser, String text, Integer commandType, Integer stepNumber) {

        try {
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(idChatUser);
            sendMessage.setText(text);

            if (commandType == 2) {
                setButton(sendMessage, commandType, stepNumber);
            }

            if (commandType == 3) {
                setButton(sendMessage, commandType, stepNumber);
            }

            this.execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void setButton(SendMessage sendMessage, Integer typeCommand, Integer stepNumber) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();


        if (typeCommand == 2 && stepNumber == 0) {
            List<Event> listEvents = eventService.getAllEventAsListFromBase();

            for (Event event : listEvents) {
                keyboardFirstRow.add(new KeyboardButton(event.getIdEvent()));
            }
        }

        if (typeCommand == 3 && stepNumber == 1) {
            keyboardFirstRow.add(new KeyboardButton(FIELD_NAME));
            keyboardFirstRow.add(new KeyboardButton(FIELD_DESCRIPTION));
            keyboardFirstRow.add(new KeyboardButton(FIELD_PLACE));
            keyboardFirstRow.add(new KeyboardButton(FIELD_NOTIFY));
        }

        if (typeCommand == 3 && stepNumber == 0) {

            for (Event event : eventService.getAllEventAsListFromBase()) {
                keyboardFirstRow.add(new KeyboardButton(event.getIdEvent()));
            }
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

}
