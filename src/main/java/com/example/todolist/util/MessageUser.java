package com.example.todolist.util;

import com.example.todolist.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageUser {
    private static final SendMessage sendMessage = new SendMessage();

    private MessageUser() {
    }
    public static void send(String idChatUser, String text) {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(idChatUser);
        sendMessage.setText(text);

        try {
            new Bot().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
