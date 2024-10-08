package com.education.hh_telegram_bot.telegram;

import com.education.hh_telegram_bot.telegram.configurations.TelegramBotKeyComponent;
import com.education.hh_telegram_bot.telegram.facades.MessageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;

    public TelegramBot(TelegramBotKeyComponent telegramBotKeyComponent) {
        super(telegramBotKeyComponent.getKey());
    }
    @Autowired
    private MessageFacade messageFacade;

    @Override
    public void onUpdateReceived(Update update) {
        messageFacade.handleUpdate(update);
    }

    public void sendMessage(BotApiMethod sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAllMessages(List<BotApiMethod> sendMessages) throws InterruptedException {
        try {
            for (BotApiMethod message: sendMessages) {
                execute(message);
                Thread.sleep(1000);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Message sendReturnedMessage(SendMessage sendMessage) {
        try {
            return execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
