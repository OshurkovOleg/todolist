package com.example.todolist;

import com.example.todolist.services.EventAddService;
import com.example.todolist.services.EventDeleteService;
import com.example.todolist.services.EventEditService;
import com.example.todolist.services.EventPrintAllService;
import com.example.todolist.util.StepAndTypeCommandBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    private final String name = "EventLogBot";
    private final String token = "5610627690:AAHBq3KzQr2WUKU5oOMCDu9nfHQzhPJAoTA";
    private EventEditService eventEditService;
    private EventAddService eventAddService;
    private EventDeleteService eventDeleteService;
    private EventPrintAllService eventPrintAllService;
    public static ArrayList<String> listUserAnswer = new ArrayList<>();
    public static int typeCommand = 0;
    public static int stepPosition = 0;

    @Autowired
    public Bot(EventEditService eventEditService, EventAddService eventAddService,
               EventDeleteService eventDeleteService, EventPrintAllService eventPrintAllService) {
        this.eventEditService = eventEditService;
        this.eventAddService = eventAddService;
        this.eventDeleteService = eventDeleteService;
        this.eventPrintAllService = eventPrintAllService;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Bot started");
        } catch (TelegramApiException e) {
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

            if (textMessage.contains("/add")) {
                StepAndTypeCommandBot.reset();
                typeCommand = 1;
                eventAddService.add(chatIdUser, stepPosition, listUserAnswer, this::send);
            }

            if (textMessage.contains("/delete")) {
                StepAndTypeCommandBot.reset();
                typeCommand = 2;
                eventDeleteService.delete(chatIdUser, stepPosition, textMessage, this::send);
            }

            if (textMessage.contains("/edit")) {
                StepAndTypeCommandBot.reset();
                typeCommand = 3;
                eventEditService.edit(chatIdUser, stepPosition, textMessage, this::send);
            }

            if (textMessage.contains("/print")) {
                StepAndTypeCommandBot.reset();
                typeCommand = 4;
                eventPrintAllService.printAll(chatIdUser, this::send);
            }


        } else {

            if (typeCommand == 1) {
                listUserAnswer.add(textMessage);
                eventAddService.add(chatIdUser, stepPosition, listUserAnswer, this::send);
            }

            if (typeCommand == 2) {
                eventDeleteService.delete(chatIdUser, stepPosition, textMessage, this::send);
            }

            if (typeCommand == 3) {
                eventEditService.edit(chatIdUser, stepPosition, textMessage, this::send);
            }
        }
    }

    final SendMessage sendMessage = new SendMessage();

    private void send(String idChatUser, String text) {

        try {
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(idChatUser);
            sendMessage.setText(text);
            this.execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}




/*
    public void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // создаем клавиатуру 1
        sendMessage.setReplyMarkup(replyKeyboardMarkup); //связываем сообщение с клавиатурой
        replyKeyboardMarkup.setSelective(true); // параметр отображения клавиатуры
        replyKeyboardMarkup.setResizeKeyboard(true); //авто-подгон клавиатуры под кнопки для пользователя
        replyKeyboardMarkup.setOneTimeKeyboard(true); //скрывать или не скрывать клавиатуру после выбора пользователя

        List<KeyboardRow> keyboardRowList = new ArrayList<>(); //создаем список контейнеров с кнопками 2

        KeyboardRow keyboardFirstRow = new KeyboardRow(); //создаем контейнер для кнопок
        keyboardFirstRow.add(new KeyboardButton("Первая кнопка")); // добавляем кнопку в контейнер keyboardFirstRow 3
        keyboardFirstRow.add(new KeyboardButton("Вторая кнопка")); // добавляем кнопку в контейнер keyboardFirstRow 3

        keyboardRowList.add(keyboardFirstRow); // в список контейнеров добавляем наш контейнер keyboardFirstRow с созданными кнопками
        replyKeyboardMarkup.setKeyboard(keyboardRowList); // добавили в клавиатуру список контейнеров с кнопками 4


    }*/
