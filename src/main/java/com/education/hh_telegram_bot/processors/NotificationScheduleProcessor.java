package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationScheduleProcessor implements ScheduleProcessor{
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final VacancyService vacancyService;
    private final WorkFilterService workFilterService;
    private final int MAX_EXCEPTION = 3;

    @Override
    @Transactional
    public void process() {
        List<UserEntity> userEntityList = userService.getAllUsers();
        int countException = 0;
        for (UserEntity user: userEntityList) {
            if (countException > MAX_EXCEPTION) {
                log.error("NotificationScheduleProcessor: terminated due to errors");
                break;
            }
            try {
                long chatId = user.getChatId();
                List<Vacancy> vacancyList = new ArrayList<>();
                Set<WorkFilter> workFilters = user.getWorkFilters();
                for (WorkFilter workFilter: workFilters) {
                    vacancyList.addAll(
                            vacancyService.getAllUnsentVacanciesByWorkFilterId(
                                    workFilter.getId())
                    );
                }
                for (Vacancy vacancy: vacancyList) {
                    Message message = telegramBot.sendReturnedMessage(
                            SendMessage.builder()
                                    .chatId(chatId)
                                    .text(vacancy.getGeneratedDescription())
                                    .build()
                    );
                    if (message != null) {
                        vacancy.setSent(true);
                        vacancyService.save(vacancy);
                    }
                }
            } catch (Exception e) {
                log.error("NotificationScheduleProcessor: telegram send message error!", e);
                countException++;
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
