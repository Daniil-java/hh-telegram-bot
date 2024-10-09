package com.education.hh_telegram_bot.processors;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
import com.education.hh_telegram_bot.services.feign.HhApiFeignService;
import com.education.hh_telegram_bot.services.VacancyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class VacancyScheduleProcessor implements ScheduleProcessor {
    private final VacancyService vacancyService;
    private final HhApiFeignService hhApiFeignService;

    private final int MAX_EXCEPTION = 3;
    @Override
    public void process() {
        List<Vacancy> vacancyList = vacancyService.getAllUnprocessedVacancies();
        int countException = 0;
        for (Vacancy vacancy: vacancyList) {
            if (countException > MAX_EXCEPTION) {
                log.error(getClass().getSimpleName() + " terminated due to errors");
                break;
            }
            try {
                HhResponseDto responseDto = hhApiFeignService
                        .getVacancyByHhId(vacancy.getHhId());

                vacancy.setName(responseDto.getName())
                        .setExperience(responseDto.getExperience().getName())
                        .setEmployment(responseDto.getEmployment().getName())
                        .setDescription(responseDto.getDescription());

                vacancyService.save(vacancy);
            } catch (Exception e) {
                log.error(getClass().getName(), e);
                countException++;
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }


}
