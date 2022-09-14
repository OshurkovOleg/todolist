package com.example.todolist.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class BotProperty {
    @Value("name")
    String name;
    @Value("token")
    String token;
}
