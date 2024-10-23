package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.TelegramService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultUpdateHandler implements UpdateHandler {
    @Autowired
    private WorkFilterService workFilterService;
    @Autowired
    @Lazy
    private TelegramService telegramService;
    private final static String RESPONSE_SUCCESS = "Ссылка сохранена";
    private final static String RESPONSE_FAIL = "Произошла ошибка";
    @Override
    public void handle(Update update, UserEntity userEntity) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();

        String answer;
        if (isValidatedUrl(messageText)) {
            workFilterService.create(userEntity, messageText);
            answer = RESPONSE_SUCCESS;
        } else {
            answer = RESPONSE_FAIL;
        }

        telegramService.sendReturnedMessage(
                chatId,
                answer,
                null, null
        );
    }

    private boolean isValidatedUrl(String text) {
        return text.startsWith("hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/search/vacancy");
    }

    @Override
    public String getHandlerListName() {
        return null;
    }
}
