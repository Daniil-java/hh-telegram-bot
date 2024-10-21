package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.TelegramService;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class InputMessageHandler {
    @Autowired
    private WorkFilterService workFilterService;
    @Autowired
    @Lazy
    private TelegramService telegramService;
    @Autowired
    private UserService userService;

    private final static String USER_INFO_COMMAND = "/about";

    public void processInputMessage(Message message, UserEntity userEntity) {
        Long chatId = message.getChatId();
        String messageText = message.getText();

        String answer = null;
        if (isValidatedUrl(messageText)) {
            workFilterService.create(userEntity.getId(), messageText);
            answer = "Ссылка сохранена";
        } else if (messageText.startsWith(USER_INFO_COMMAND)) {
            userService.save(userEntity.setInfo(messageText.substring(USER_INFO_COMMAND.length())));
            answer = "Информация сохранена";
        }

        if (answer != null) {
            telegramService.sendReturnedMessage(
                    chatId,
                    answer,
                    null, null
            );
        }
    }

    private boolean isValidatedUrl(String text) {
        return text.startsWith("hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/search/vacancy");
    }
}
