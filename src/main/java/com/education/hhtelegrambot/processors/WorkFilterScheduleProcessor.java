package com.education.hhtelegrambot.processors;


import com.education.hhtelegrambot.entities.WorkFilter;
import com.education.hhtelegrambot.dtos.hh.HhSimpleResponseDto;
import com.education.hhtelegrambot.services.VacancyService;
import com.education.hhtelegrambot.services.WorkFilterService;
import com.education.hhtelegrambot.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class WorkFilterScheduleProcessor implements ScheduleProcessor{

    private final WorkFilterService workFilterService;
    private final VacancyService vacancyService;

    @Override
    public void process() {
        //Формирование общего листа пользовательских ссылок
        List<WorkFilter> workFilterList = workFilterService.getAll();
        //Загрузка, парсинг и сохранение в БД id вакансий
        for (WorkFilter workFilter: workFilterList) {
            //Получение ДТО вакансий
            List<HhSimpleResponseDto> hhSimpleResponseDtos =
                    workFilterService.loadHhVacancies(workFilter);
            //Парсинг полученных вакансий
            vacancyService.parseHhVacancies(hhSimpleResponseDtos, workFilter);
            ThreadUtil.sleep(1000);
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }

}
