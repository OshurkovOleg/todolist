package com.example.todolist;

import com.example.todolist.model.Event;
import com.example.todolist.model.History;
import com.example.todolist.model.Person;
import com.example.todolist.property.BotProperty;
import com.example.todolist.services.*;
import com.example.todolist.util.StepAndTypeCommandBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.todolist.constants.Constants.*;


@Slf4j
@EnableScheduling
@Component
public class Bot extends TelegramLongPollingBot {

    private EventEditService eventEditService;
    private EventAddService eventAddService;
    private EventDeleteService eventDeleteService;
    private EventPrintAllService eventPrintAllService;
    private EventPrintHistoryService eventPrintHistoryService;
    private EventPrintNextService eventPrintNextService;
    private EventPrintBetweenDateService eventPrintBetweenDateService;
    private EventService eventService;
    private PersonService personService;
    private HistoryService historyService;
    private BotProperty botProperty;
    public static ArrayList<String> listUserAnswer = new ArrayList<>();
    final SendMessage sendMessage = new SendMessage();
    public static int commandType = 0;
    public static int stepNumber = 0;

    public Bot() {
    }

    @Autowired
    private Bot(EventEditService eventEditService, EventAddService eventAddService,
                EventDeleteService eventDeleteService, EventPrintAllService eventPrintAllService,
                EventPrintNextService eventPrintNextService, EventService eventService,
                EventPrintBetweenDateService eventPrintBetweenDateService, BotProperty botProperty,
                PersonService personService, HistoryService historyService, EventPrintHistoryService eventPrintHistoryService) {

        this.eventEditService = eventEditService;
        this.eventAddService = eventAddService;
        this.eventDeleteService = eventDeleteService;
        this.eventPrintAllService = eventPrintAllService;
        this.eventPrintNextService = eventPrintNextService;
        this.eventService = eventService;
        this.eventPrintBetweenDateService = eventPrintBetweenDateService;
        this.botProperty = botProperty;
        this.personService = personService;
        this.historyService = historyService;
        this.eventPrintHistoryService = eventPrintHistoryService;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info(BOT_STARTED);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return botProperty.getName();
    }

    @Override
    public String getBotToken() {
        return botProperty.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        String chatIdUserStr = update.getMessage().getChatId().toString();
        long chatIdLong = Long.parseLong(chatIdUserStr);
        String textMessage = update.getMessage().getText();

        userInitialization(chatIdUserStr, update);


        if (textMessage.contains(FORWARD_SLASH)) {

            if (textMessage.equals(ADD)) {
                StepAndTypeCommandBot.reset();
                commandType = 1;
                eventAddService.add(chatIdLong, listUserAnswer, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(DELETE)) {
                StepAndTypeCommandBot.reset();
                commandType = 2;
                eventDeleteService.delete(chatIdLong, textMessage, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(EDIT)) {
                StepAndTypeCommandBot.reset();
                commandType = 3;
                eventEditService.edit(chatIdLong, textMessage, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(PRINT)) {
                StepAndTypeCommandBot.reset();
                commandType = 4;
                eventPrintAllService.printAll(chatIdLong, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(PRINT_NEXT_EVENT)) {
                StepAndTypeCommandBot.reset();
                commandType = 5;
                eventPrintNextService.print(chatIdLong, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(PRINT_EVENT_BY_DATE)) {
                StepAndTypeCommandBot.reset();
                commandType = 6;
                eventPrintBetweenDateService.print(chatIdLong, listUserAnswer, this::send, commandType, stepNumber);
            }

            if (textMessage.equals(PRINT_HISTORY)) {
                StepAndTypeCommandBot.reset();
                commandType = 7;
                eventPrintHistoryService.printHistory(chatIdLong, this::send, commandType, stepNumber);
            }


        } else {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (commandType == 1) {
                listUserAnswer.add(textMessage);
                eventAddService.add(chatIdLong, listUserAnswer, this::send, commandType, stepNumber);
            }

            if (commandType == 2) {
                eventDeleteService.delete(chatIdLong, textMessage, this::send, commandType, stepNumber);
            }

            if (commandType == 3) {
                eventEditService.edit(chatIdLong, textMessage, this::send, commandType, stepNumber);
            }

            if (commandType == 6) {
                listUserAnswer.add(textMessage);
                eventPrintBetweenDateService.print(chatIdLong, listUserAnswer, this::send, commandType, stepNumber);
            }
        }

    }
    private void userInitialization(String chat, Update update) {
        long chat_id = Long.parseLong(chat);
        Person person = personService.getByChatId(chat_id);
        if (person == null) {
            personService.save(getPersonInfo(chat_id, update));
            log.info(USER_SUCCESSFULLY_SAVED_TO_BASE);
        }
    }

    private Person getPersonInfo(Long chatId, Update update) {
        String firstName = update.getMessage().getFrom().getFirstName();
        String LastName = update.getMessage().getFrom().getLastName();
        String UserName = update.getMessage().getFrom().getUserName();
        String LanguageCode = update.getMessage().getFrom().getLanguageCode();
        return new Person(chatId, firstName, LastName, UserName, LanguageCode);
    }


    private void send(Long idChatUser, String text, Integer commandType, Integer stepNumber) {

        try {
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(idChatUser);
            sendMessage.setText(text);

            if (commandType == 2) {
                setButton(sendMessage, commandType, stepNumber, idChatUser);
            }

            if (commandType == 3) {
                if (!(text.equalsIgnoreCase(EVENT_MISSING_BY_ID))) {
                    setButton(sendMessage, commandType, stepNumber, idChatUser);
                }
            }

            this.execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void notifyByDate() {
        if (eventService.getAllEventIsAfterDateNow().size() != 0) {
            for (Event event : eventService.getAllEventIsAfterDateNow()) {
                long chatID = event.getChatID();
                LocalDateTime startEventDate = event.getStartExecution();
                int notificationTime = event.getNotifyBeforeEventHours();

                LocalDateTime dateMinusNotifyTime = startEventDate.minusMinutes(notificationTime);

                if (LocalDateTime.now().isAfter(dateMinusNotifyTime)) {
                    if (!event.isNotifyStatus()) {
                        sendMsg(chatID, event, notificationTime);
                        event.setNotifyStatus(true);
                        eventService.save(event);
                    }
                }
            }
        }
        transferEventsToHistory();
        log.info(EVENT_SUCCESSFULLY_SAVED_TO_HISTORY);
    }

    @Transactional
    void transferEventsToHistory() {
        List<Event> allEvent = eventService.getAllEventAsListFromBase();
        for (Event event : allEvent) {
            if (event.getEndExecution().isBefore(LocalDateTime.now())) {
                historyService.save(new History(event));
                eventService.deleteEvent(event.getId());
            }
        }
    }

    private void sendMsg(Long chatID, Event event, int minutesToDate) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);
        sendMessage.setText(String.format(WARNING_TEXT_NOTIFY, minutesToDate) +
                event.toString());
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void setButton(SendMessage sendMessage, Integer typeCommand, Integer stepNumber, Long idChat) {

        ReplyKeyboardMarkup replyKeyboardMarkup = initKeyboard(sendMessage);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        if (typeCommand == 3 && stepNumber == 0) {
            for (Event event : eventService.getAllEventAsListFromBase()) {
                if (event.getChatID() == idChat) {
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardRow.add(String.valueOf(event.getId()));
                    String formatDateTime = event.getStartExecution().toString().replace("T", " ");
                    keyboardRow.add(event.getName() + " " + event.getPlace() + " " + formatDateTime);
                    keyboardRowList.add(keyboardRow);
                }
            }
        }

        if (typeCommand == 3 && stepNumber == 1) {

            KeyboardRow keyboardFirstRow1 = new KeyboardRow();
            KeyboardRow keyboardFirstRow2 = new KeyboardRow();
            KeyboardRow keyboardFirstRow3 = new KeyboardRow();
            KeyboardRow keyboardFirstRow4 = new KeyboardRow();
            KeyboardRow keyboardFirstRow5 = new KeyboardRow();
            KeyboardRow keyboardFirstRow6 = new KeyboardRow();

            keyboardFirstRow1.add(NAME_TEXT);
            keyboardFirstRow2.add(DESCRIPTION_TEXT);
            keyboardFirstRow3.add(PLACE_TEXT);
            keyboardFirstRow4.add(NOTIFY_TEXT);
            keyboardFirstRow5.add(START_EVENT_TEXT);
            keyboardFirstRow6.add(FINISH_EVENT_TEXT);

            keyboardRowList.add(keyboardFirstRow1);
            keyboardRowList.add(keyboardFirstRow2);
            keyboardRowList.add(keyboardFirstRow3);
            keyboardRowList.add(keyboardFirstRow4);
            keyboardRowList.add(keyboardFirstRow5);
            keyboardRowList.add(keyboardFirstRow6);

        }

        if (typeCommand == 2 && stepNumber == 0) {

            for (Event event : eventService.getAllEventAsListFromBase()) {
                if (event.getChatID() == idChat) {
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardRow.add(String.valueOf(event.getId()));
                    keyboardRowList.add(keyboardRow);
                }
            }
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    private static ReplyKeyboardMarkup initKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return replyKeyboardMarkup;
    }
}
