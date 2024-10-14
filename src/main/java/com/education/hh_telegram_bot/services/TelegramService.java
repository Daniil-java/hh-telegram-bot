package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {
    private final TelegramBot telegramBot;

    public Message sendReturnedVacancyMessage(Long chatId, Vacancy vacancy) {
        return telegramBot.sendReturnedMessage(SendMessage.builder()
                .chatId(chatId)
                .text(vacancyToFormattedString(vacancy))
                .build());
    }

    public String vacancyToFormattedString(Vacancy vacancy) {
        StringBuilder builder = new StringBuilder();
        builder.append(nullSafe(vacancy.getName())).append("\n");
        builder.append(nullSafe(vacancy.getDepartment())).append("\n");
        builder.append(nullSafe(vacancy.getSalary())).append("\n");
        builder.append(nullSafe(vacancy.getExperience())).append("\n");
        builder.append(nullSafe(vacancy.getGeneratedDescription())).append("\n");
        return builder.toString();
    }

    private String nullSafe(String field) {
        return field == null ? "" : field;
    }
}
