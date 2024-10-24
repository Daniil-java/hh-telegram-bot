package com.education.hhtelegrambot.telegram.handlers;

import com.education.hhtelegrambot.entities.UserEntity;
import com.education.hhtelegrambot.telegram.facades.MessageFacade;
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
