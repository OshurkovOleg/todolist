package com.example.todolist.services;

import com.example.todolist.model.History;
import com.example.todolist.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void save(History history) {
        historyRepository.save(history);
    }

    public List<History> getAllEventHistoryFromBase() {
        return historyRepository.getAllBy();
    }
}
