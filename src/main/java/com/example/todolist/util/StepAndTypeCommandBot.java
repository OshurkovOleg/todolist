package com.example.todolist.util;

import com.example.todolist.Bot;

public class StepAndTypeCommandBot {

    private StepAndTypeCommandBot() {
    }
    public static void reset() {
        Bot.stepPosition = 0;
        Bot.typeCommand = 0;
    }
}
