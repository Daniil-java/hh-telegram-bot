package com.education.hh_telegram_bot.telegram.facades;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.telegram.handlers.InputMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageFacade {
    private final UserService userService;
    private final InputMessageHandler inputMessageHandler;

    public void handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            handleInputCallback(update.getCallbackQuery());
        }else {
            handleInputMessage(update.getMessage());
        }
    }

    private void handleInputMessage(Message message) {
        UserEntity userEntity = userService.getOrCreateUser(message.getFrom());
        inputMessageHandler.processInputMessage(message, userEntity);
    }

    private BotApiMethod handleInputCallback(CallbackQuery callbackQuery) {
        //TODO: Планируется
        return null;
    }
}
