package com.example.todolist.util;

import com.example.todolist.Bot;

public class StepAndTypeCommandBot {

    private StepAndTypeCommandBot() {
    }

    public static void reset() {
        Bot.stepNumber = 0;
        Bot.commandType = 0;
    }
}
