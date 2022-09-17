package com.example.todolist.util;

@FunctionalInterface
public interface FourthConsumer<I, T, C, S> {
    void accept(I idChatUser, T text, C typeCommand, S stepNumber);
}
