package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
import com.education.hh_telegram_bot.entities.hh.HhSimpleResponseDto;
import com.education.hh_telegram_bot.repositories.VacancyRepository;
import com.education.hh_telegram_bot.services.feign.HhApiFeignService;
import com.education.hh_telegram_bot.services.feign.OpenAiApiFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyService {
    private final HhApiFeignService hhApiFeignService;
    private final VacancyRepository vacancyRepository;
    private final HhApiService hhApiService;
    private final OpenAiApiFeignService openAiApiFeignService;

    public List<Vacancy> getAllUnprocessedVacancies() {
        return vacancyRepository.findAllByNameIsNull();
    }

    public List<Vacancy> getAllUngeneratedVacancies() {
        return vacancyRepository.findAllByGeneratedDescriptionIsNull();
    }

    public List<Vacancy> ByNotificationAttemptCountLessThan(int count) {
        return vacancyRepository.findGeneratedVacanciesWithAttemptsLessThan(count);
    }

    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    public void loadAndParseHhVacancies(WorkFilter workFilter) {
        //Получение ДТО вакансий, по переденной ссылке
        List<HhSimpleResponseDto> hhSimpleResponseDtos =
                hhApiService.loadAndParseHhVacancies(workFilter);

        //Обработка полученного списка ДТО-вакансий
        for (HhSimpleResponseDto dto: hhSimpleResponseDtos) {
            //Проверка на наличие уже существующих дубликатов в БД
            if (!vacancyRepository.findByHhIdAndWorkFilterId(dto.getHhId(), workFilter.getId()).isPresent()) {
                //Конвертирование ДТО в сущность вакансии и сохранение
                vacancyRepository.save(new Vacancy()
                        .setUrl(dto.getUrl())
                        .setHhId(dto.getHhId())
                        .setWorkFilter(workFilter));
            }
        }
    }


    //Обработка незаполненых вакансий, посредством обращения к api
    public void fetchAndSaveEntity(Vacancy vacancy) {
        //Получение ДТО-вакансии обращением к api
        HhResponseDto responseDto = hhApiFeignService.getVacancyByHhId(vacancy.getHhId());
        //Конвертация ДТО в сущность вакансии и сохранение
        vacancyRepository.save(vacancy.setName(responseDto.getName())
                .setExperience(responseDto.getExperience().getName())
                .setEmployment(responseDto.getEmployment().getName())
                .setDescription(responseDto.getDescription()));
    }

    //Сохранение сгенерированного описания вакансии, посредством обращения к OpenAI API
    public void fetchGenerateDescriptionAndSaveEntity(Vacancy vacancy) {
        //Получение сгенерированного краткого описания, на основе описания полного
        String generatedDescription = openAiApiFeignService
                .generateDescription(vacancy.getDescription());
        //Обновление и сохранение данных вакансии
        vacancyRepository.save(vacancy.setGeneratedDescription(generatedDescription));
    }
}
