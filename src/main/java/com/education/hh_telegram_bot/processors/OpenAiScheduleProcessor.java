package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.feign.OpenAiApiFeignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
@Slf4j
public class OpenAiScheduleProcessor implements ScheduleProcessor {
    private final OpenAiApiFeignService openAiApiFeignService;
    private final VacancyService vacancyService;
    private final int MAX_EXCEPTION = 3;

    @Override
    public void process() {
        List<Vacancy> vacancyList = vacancyService.getAllUngeneratedVacancies();

        int countException = 0;
        for (Vacancy vacancy: vacancyList) {
            if (countException > MAX_EXCEPTION) {
                log.error(getClass().getSimpleName() + " terminated due to errors");
                break;
            }
            try {
                String generatedDescription = openAiApiFeignService.generateDescription(vacancy.getDescription());
                vacancy.setGeneratedDescription(generatedDescription);
                vacancyService.save(vacancy);
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
