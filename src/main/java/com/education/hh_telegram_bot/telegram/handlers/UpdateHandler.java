package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.telegram.facades.MessageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update, UserEntity userEntity);

    String getHandlerListName();

    @Autowired
    default void registerMyself(MessageFacade messageFacade) {
        messageFacade.register(getHandlerListName(), this);
    }

}
