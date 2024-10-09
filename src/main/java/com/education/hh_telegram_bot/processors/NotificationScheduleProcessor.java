package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

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
    public void process() {
        List<UserEntity> userEntityList = userService.getAllUsers();
        int countException = 0;
        for (UserEntity user: userEntityList) {
            if (countException > MAX_EXCEPTION) {
                log.error(getClass().getSimpleName() + " terminated due to errors");
                break;
            }
            try {
                long chatId = user.getChatId();
                List<Vacancy> vacancyList = new ArrayList<>();
                List<WorkFilter> workFilters = workFilterService.getAllByUserId(user.getId());
                for (WorkFilter workFilter: workFilters) {
                    vacancyList.addAll(vacancyService.getAllUnsentVacancies(workFilter.getId()));
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
                log.error(getClass().getSimpleName(), e);
                countException++;
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
