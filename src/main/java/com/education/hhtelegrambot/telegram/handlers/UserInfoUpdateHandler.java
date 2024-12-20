package com.education.hhtelegrambot.telegram.handlers;

import com.education.hhtelegrambot.entities.UserEntity;
import com.education.hhtelegrambot.services.TelegramService;
import com.education.hhtelegrambot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserInfoUpdateHandler implements UpdateHandler {
    @Autowired
    @Lazy
    private TelegramService telegramService;
    @Autowired
    private UserService userService;

    private final static String USER_INFO_COMMAND = "/about";
    private final static String RESPONSE = "Информация сохранена";

    @Override
    public void handle(Update update, UserEntity userEntity) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();

        userService.save(userEntity.setInfo(messageText.substring(USER_INFO_COMMAND.length())));
        telegramService.sendReturnedMessage(
                chatId,
                RESPONSE,
                null, null
        );
    }

    @Override
    public String getHandlerListName() {
        return USER_INFO_COMMAND;
    }
}
