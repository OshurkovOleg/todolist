package com.example.todolist.services;

@FunctionalInterface
public interface FourthConsumer<I, T, C, S> {
    void accept(I idChatUser, T text, C typeCommand, S stepNumber);
}
