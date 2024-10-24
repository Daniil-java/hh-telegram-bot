package com.education.hhtelegrambot.services;

import com.education.hhtelegrambot.entities.Vacancy;
import com.education.hhtelegrambot.entities.VacancyStatus;
import com.education.hhtelegrambot.telegram.TelegramBot;
import com.education.hhtelegrambot.telegram.handlers.CallbackHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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

    public Message sendReturnedMessage(long chatId, String text,
                                       InlineKeyboardMarkup inlineKeyboardMarkup, Integer replyMessageId) {
        return telegramBot.sendReturnedMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(inlineKeyboardMarkup)
                        .replyToMessageId(replyMessageId)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .build()
        );
    }

    public Message sendReturnedVacancyMessage(Long chatId, Vacancy vacancy) {
        return telegramBot.sendReturnedMessage(SendMessage.builder()
                .chatId(chatId)
                .text(vacancyToFormattedString(vacancy))
                .replyMarkup(getInlineMessageButtonLetterGenerate(vacancy.getId()))
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build());
    }

    private InlineKeyboardMarkup getInlineMessageButtonLetterGenerate(long vacancyId) {
        String callbackCommand = CallbackHandler.REQUEST_COMMAND;
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generateButton =
                new InlineKeyboardButton("Принять");
        InlineKeyboardButton rejectButton =
                new InlineKeyboardButton("Отклонить");
        generateButton.setCallbackData(callbackCommand + " " + VacancyStatus.APPLIED.name() + vacancyId);
        rejectButton.setCallbackData(callbackCommand + " " + VacancyStatus.REJECTED.name() + vacancyId);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(Arrays.asList(generateButton, rejectButton));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String vacancyToFormattedString(Vacancy vacancy) {
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>").append(nullSafe(vacancy.getName())).append("</strong>");
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
