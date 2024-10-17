package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {
    private final TelegramBot telegramBot;

    public Message sendReturnedMessage(SendMessage message) {
        return telegramBot.sendReturnedMessage(message);
    }

    public Message sendReturnedVacancyMessage(Long chatId, Vacancy vacancy) {
        return telegramBot.sendReturnedMessage(SendMessage.builder()
                .chatId(chatId)
                .text(vacancyToFormattedString(vacancy))
                .replyMarkup(getInlineMessageButtonLetterGenerate(vacancy.getHhId()))
                .build());
    }

    private InlineKeyboardMarkup getInlineMessageButtonLetterGenerate(long vacancyId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generateButton =
                new InlineKeyboardButton("Сгенерировать сопроводительное письмо");
        generateButton.setCallbackData("coverLetter" + vacancyId);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(Arrays.asList(generateButton));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String vacancyToFormattedString(Vacancy vacancy) {
        StringBuilder builder = new StringBuilder();
        builder.append(nullSafe(vacancy.getName()));
        builder.append(nullSafe(vacancy.getDepartment()));
        builder.append(nullSafe(vacancy.getSalary()));
        builder.append(nullSafe(vacancy.getExperience()));
        builder.append(nullSafe(vacancy.getUrl()));
        builder.append(nullSafe(vacancy.getKeySkills()));
        builder.append(nullSafe(vacancy.getGeneratedDescription()));
        return builder.toString().trim();
    }

    private String nullSafe(String field) {
        return field == null ? "" : field + "\n";
    }
}
