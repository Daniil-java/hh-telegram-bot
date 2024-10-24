package com.education.hhtelegrambot.telegram.handlers;

import com.education.hhtelegrambot.entities.UserEntity;
import com.education.hhtelegrambot.entities.VacancyStatus;
import com.education.hhtelegrambot.services.TelegramService;
import com.education.hhtelegrambot.services.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackHandler implements UpdateHandler {
    private static final String APPLIED_COMMAND = VacancyStatus.APPLIED.name();
    private static final String REJECTED_COMMAND = VacancyStatus.REJECTED.name();

    public static final String REQUEST_COMMAND = "/decision";
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    @Lazy
    private TelegramService telegramService;

    @Override
    public void handle(Update update, UserEntity userEntity) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();

        String decision = callbackData.split(" ")[1];
        if (decision.equals(APPLIED_COMMAND)) {
            long vacancyId = Long.parseLong(callbackData.substring(APPLIED_COMMAND.length()));
            String coverLetter = vacancyService.fetchGenerateCoverLetter(vacancyId, userEntity.getInfo());
            //Отправка сообщения пользователю и обработка, в случае удачного отправления
            if (telegramService.sendReturnedMessage(chatId, coverLetter,
                    null, messageId) != null) {
                vacancyService.updateStatusById(vacancyId, VacancyStatus.APPLIED);
            }
        } else if (decision.equals(REJECTED_COMMAND)) {
            long vacancyId = Long.parseLong(callbackData.substring(REJECTED_COMMAND.length()));
            vacancyService.vacancyRejectById(vacancyId);
        }
    }

    @Override
    public String getHandlerListName() {
        return "/decision";
    }
}
