package com.education.hh_telegram_bot.telegram.facades;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.telegram.handlers.CallBackHandler;
import com.education.hh_telegram_bot.telegram.handlers.InputMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageFacade {
    private final UserService userService;
    private final InputMessageHandler inputMessageHandler;
    private final CallBackHandler callBackHandler;

    public void handleUpdate(Update update) {
        /*
         На этапе разработки обработка сообщений
         происходит только от одного пользователя
         */
        User user = update.getMessage() != null ?
                update.getMessage().getFrom() :
                update.getCallbackQuery().getFrom();
        if (user.getId() != 425120436L) return;

        UserEntity userEntity = userService.getOrCreateUser(user);
        if (update.hasCallbackQuery()) {
            callBackHandler.processCallbackQuery(update.getCallbackQuery(), userEntity);
        } else {
            inputMessageHandler.processInputMessage(update.getMessage(), userEntity);
        }
    }
}
