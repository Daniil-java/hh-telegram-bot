package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class NotificationScheduleProcessor implements ScheduleProcessor{
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final VacancyService vacancyService;
    private final WorkFilterService workFilterService;

    @Override
    public void process() {
        List<UserEntity> userEntityList = userService.getAllUsers();
        for (UserEntity user: userEntityList) {
            long chatId = user.getChatId();
            List<Vacancy> vacancyList = new ArrayList<>();
            List<WorkFilter> workFilters = workFilterService.getAllByUserId(user.getId());
            for (WorkFilter workFilter: workFilters) {
                vacancyList.addAll(vacancyService.getAllUnsentVacancies(workFilter.getId()));
            }
            List<BotApiMethod> sendMessages = new ArrayList<>();
            for (Vacancy vacancy: vacancyList) {
                sendMessages.add(SendMessage.builder()
                        .chatId(chatId)
                        .text(vacancy.getGeneratedDescription())
                        .build()
                );
            }
            try {
                telegramBot.sendAllMessages(sendMessages);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
