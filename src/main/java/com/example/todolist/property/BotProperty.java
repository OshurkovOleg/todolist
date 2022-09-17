package com.example.todolist.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class BotProperty {
    private  String name;
    private  String token;

}
