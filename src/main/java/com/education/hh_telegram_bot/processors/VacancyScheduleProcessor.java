package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.services.feign.HhApiFeignService;
import com.education.hh_telegram_bot.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class VacancyScheduleProcessor implements ScheduleProcessor {
    private final VacancyService vacancyService;
    private final HhApiFeignService hhApiFeignService;

    @Override
    public void process() {
        List<Vacancy> vacancyList = vacancyService.getAllUnprocessedVacancies();
        for (int i = 0; i < vacancyList.size(); i++) {
            Vacancy vacancy = vacancyList.get(i);
            Vacancy updatedVacancy = hhApiFeignService
                    .getVacancyByHhId(String.valueOf(vacancy.getHhId()))
                    .setId(vacancy.getId())
                    .setWorkFilter(vacancy.getWorkFilter())
                    .setUrl(vacancy.getUrl());
            vacancyList.set(i, updatedVacancy);
        }
        vacancyService.saveAll(vacancyList);
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }


}
