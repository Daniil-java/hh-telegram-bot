package com.education.hhtelegrambot.telegram.configurations;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class TelegramBotKeyComponent {
    private final String key;

    @Autowired
    public TelegramBotKeyComponent(Environment environment) {
        this.key = environment.getProperty("HHTGBOT_TOKEN");
        log.info("Generation key initiated (workhunttg_bot)");
    }
}
