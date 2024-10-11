package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.WorkFilterService;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InputMessageHandler {
    @Autowired
    private WorkFilterService workFilterService;
    @Autowired
    @Lazy
    private TelegramBot telegramBot;

    public void processInputMessage(Message message, UserEntity userEntity) {
        Long chatId = message.getChatId();
        String url = message.getText();
        if (isValidated(url)) {
            workFilterService.save(userEntity.getId(), url);
            telegramBot.sendMessage(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text("Ссылка сохранена")
                            .build()
            );
        }
    }

    private boolean isValidated(String text) {
        return text.startsWith("hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/vacancies/");
    }
}
