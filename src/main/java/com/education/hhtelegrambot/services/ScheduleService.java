package com.education.hhtelegrambot.services;

import com.education.hhtelegrambot.processors.NotificationScheduleProcessor;
import com.education.hhtelegrambot.processors.OpenAiScheduleProcessor;
import com.education.hhtelegrambot.processors.VacancyScheduleProcessor;
import com.education.hhtelegrambot.processors.WorkFilterScheduleProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {
    private final WorkFilterScheduleProcessor workFilterScheduleProcessor;
    private final OpenAiScheduleProcessor openAiScheduleProcessor;
    private final VacancyScheduleProcessor vacancyScheduleProcessor;
    private final NotificationScheduleProcessor notificationScheduleProcessor;

    //TODO: Шедулеры требует пересмотра времени срабатывания
//    @Scheduled(initialDelay = 1000)
    @Scheduled(cron = "0 */5 * * * *")
    public void workFilterScheduleProcess() {
        getInfo(workFilterScheduleProcessor.getSchedulerName());
        workFilterScheduleProcessor.process();
    }


    @Scheduled(cron = "0 */15 * * * *")
    public void openAiScheduleProcess() {
        getInfo(openAiScheduleProcessor.getSchedulerName());
        openAiScheduleProcessor.process();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void vacancyScheduleProcess() {
        getInfo(vacancyScheduleProcessor.getSchedulerName());
        vacancyScheduleProcessor.process();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void notificationScheduleProcess() {
        getInfo(notificationScheduleProcessor.getSchedulerName());
        notificationScheduleProcessor.process();
    }

    private void getInfo(String name) {
        log.info(name + " started working");
    }
}
