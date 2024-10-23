package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.VacancyStatus;
import com.education.hh_telegram_bot.services.TelegramService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.utils.ThreadUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationScheduleProcessor implements ScheduleProcessor {
    private final VacancyService vacancyService;
    private final TelegramService telegramService;
    private static final int MAX_ATTEMPT_COUNT = 3;

    @Override
    @Transactional
    public void process() {
        //Получение неотправлленых пользователю вакансий
        List<Vacancy> vacancyList = vacancyService.findByNotificationAttemptCountLessThan(MAX_ATTEMPT_COUNT);
        for (Vacancy vacancy : vacancyList) {
            long chatId = vacancy.getWorkFilter().getUser().getChatId();
            //Проверка состояния отправленного сообщения
            if (telegramService.sendReturnedVacancyMessage(chatId, vacancy) != null) {
                vacancy.setStatus(VacancyStatus.NOTIFICATED);
            } else {
                vacancy.setNotificationAttemptCount(vacancy.getNotificationAttemptCount() + 1);
                if (vacancy.getNotificationAttemptCount() > MAX_ATTEMPT_COUNT) {
                    vacancy.setStatus(VacancyStatus.NOTIFICATION_ERROR);
                }
            }
            //Сохранение статуса отправки вакансии
            vacancyService.save(vacancy);
            ThreadUtil.sleep(1000);
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
