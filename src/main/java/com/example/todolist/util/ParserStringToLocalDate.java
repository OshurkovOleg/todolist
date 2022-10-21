package com.example.todolist.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParserStringToLocalDate {

    private ParserStringToLocalDate() {
    }
    public static LocalDateTime parsing(String str) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(str, dtf);
    }
}
