package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.feign.OpenAiApiFeignService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class OpenAiScheduleProcessor implements ScheduleProcessor {
    private final OpenAiApiFeignService openAiApiFeignService;
    private final VacancyService vacancyService;

    @Override
    public void process() {
        List<Vacancy> vacancyList = vacancyService.getAllUngeneratedVacancies();
        for (Vacancy vacancy: vacancyList) {
            String generatedDescription = openAiApiFeignService.generateDescription(vacancy.getDescription());
            vacancy.setGeneratedDescription(generatedDescription);
        }
        vacancyService.saveAll(vacancyList);
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }
}
