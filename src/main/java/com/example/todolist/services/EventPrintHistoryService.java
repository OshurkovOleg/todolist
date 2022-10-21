package com.example.todolist.services;

import com.example.todolist.model.History;
import com.example.todolist.util.FourthConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.example.todolist.constants.Constants.*;

@Slf4j
@Service
public class EventPrintHistoryService {
    HistoryService historyService;

    @Autowired
    public EventPrintHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void printHistory(Long idChat, FourthConsumer<Long, String, Integer, Integer> sendMsg,
                             Integer commandType, Integer stepNumber) {

        historyService.getAllEventHistoryFromBase().stream()
                .filter(event -> event.getChatID() == idChat)
                .sorted(Comparator.comparing(History::getStartExecution))
                .forEach(event -> sendMsg.accept(idChat, event.toString(), commandType, stepNumber));
        log.info(SELECTION_HISTORY_EVENTS_MADE);
    }


}
