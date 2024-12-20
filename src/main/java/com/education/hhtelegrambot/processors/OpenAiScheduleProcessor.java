package com.education.hhtelegrambot.processors;

import com.education.hhtelegrambot.entities.Vacancy;
import com.education.hhtelegrambot.entities.VacancyStatus;
import com.education.hhtelegrambot.services.VacancyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
@Slf4j
public class OpenAiScheduleProcessor implements ScheduleProcessor {
    private final VacancyService vacancyService;
    private final static int MAX_EXCEPTION = 3;

    @Override
    public void process() {
        //Получение вакансий без сгенерированного описания
        List<Vacancy> vacancyList = vacancyService.getAllByVacancyStatus(VacancyStatus.PARSED);

        //Счетчик ошибок
        int countException = 0;
        for (Vacancy vacancy: vacancyList) {
            if (countException > MAX_EXCEPTION) {
                log.error("OpenAiScheduleProcessor: terminated due to errors");
                break;
            }
            try {
                //Обработка вакансии для генерации описания
                vacancyService.fetchGenerateDescriptionAndUpdateEntity(vacancy);
            } catch (Exception e) {
                log.error("OpenAiScheduleProcessor: generation error!", e);
                countException++;
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
