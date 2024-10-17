package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.TelegramService;
import com.education.hh_telegram_bot.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackHandler {
    private static final String CB_COMMAND = "coverLetter";
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    @Lazy
    private TelegramService telegramService;

    public void processCallbackQuery(CallbackQuery callbackQuery, UserEntity userEntity) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();
        /*
            Расриширение приложения не планируется,
            потому динамическая подмена реализации не применяется
         */
        if (callbackData.startsWith(CB_COMMAND)) {
            long vacancyHhId = Long.parseLong(callbackData.substring(CB_COMMAND.length()));
            String coverLetter = vacancyService.fetchGenerateCoverLetter(vacancyHhId);
            telegramService.sendReturnedMessage(chatId, coverLetter, null, messageId);
        }
    }
}
