package com.education.hhtelegrambot.processors;

import com.education.hhtelegrambot.entities.Vacancy;
import com.education.hhtelegrambot.entities.VacancyStatus;
import com.education.hhtelegrambot.services.VacancyService;
import com.education.hhtelegrambot.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class VacancyScheduleProcessor implements ScheduleProcessor {
    private final VacancyService vacancyService;
    private final static int MAX_EXCEPTION = 3;

    @Override
    public void process() {
        //Загрузка необработанных вакансий
        List<Vacancy> vacancyList = vacancyService.getAllByVacancyStatus(VacancyStatus.CREATED);

        //Счетчик возникших исключений
        int countException = 0;
        for (Vacancy vacancy: vacancyList) {
            if (countException > MAX_EXCEPTION) {
                log.error("VacancyScheduleProcessor: terminated due to errors");
                break;
            }
            try {
                //Обработка вакансий
                vacancyService.fetchAndSaveEntity(vacancy);
                ThreadUtil.sleep(100);
            } catch (Exception e) {
                log.error("VacancyScheduleProcessor: HH API error!", e);
                countException++;
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }


}
