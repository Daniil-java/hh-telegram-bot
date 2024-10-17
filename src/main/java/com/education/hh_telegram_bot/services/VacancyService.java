package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
import com.education.hh_telegram_bot.entities.hh.HhSimpleResponseDto;
import com.education.hh_telegram_bot.repositories.VacancyRepository;
import com.education.hh_telegram_bot.services.feign.OpenAiApiFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyService {
    private final VacancyRepository vacancyRepository;
    private final HhApiService hhApiService;
    private final OpenAiApiFeignService openAiApiFeignService;

    public List<Vacancy> getAllUnprocessedVacancies() {
        return vacancyRepository.findAllByNameIsNull();
    }

    public List<Vacancy> getAllUngeneratedVacancies() {
        return vacancyRepository.findAllByGeneratedDescriptionIsNullAndDescriptionIsNotNull();
    }

    public List<Vacancy> findByNotificationAttemptCountLessThan(int count) {
        return vacancyRepository.findGeneratedVacanciesWithAttemptsLessThan(count);
    }

    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    public void parseHhVacancies(List<HhSimpleResponseDto> hhSimpleResponseDtos, WorkFilter workFilter) {
        //Обработка полученного списка ДТО-вакансий
        for (HhSimpleResponseDto dto: hhSimpleResponseDtos) {
            //Проверка на наличие уже существующих дубликатов в БД
            if (!vacancyRepository.findByHhIdAndWorkFilterId(dto.getHhId(), workFilter.getId())
                    .isPresent()) {
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
        HhResponseDto responseDto = hhApiService.getVacancyByHhId(vacancy.getHhId());
        //Конвертация keySkills в String
        StringBuilder builder = new StringBuilder();
        for (String skill: responseDto.getKeySkills()) {
            builder.append(skill).append("|");
        }
        //Конвертация ДТО в сущность вакансии и сохранение
        vacancyRepository.save(vacancy.setName(responseDto.getName())
                .setExperience(responseDto.getExperience().getName())
                .setKeySkills(builder.toString())
                .setEmployment(responseDto.getEmployment().getName())
                .setDescription(responseDto.getDescription()));
    }

    //Сохранение сгенерированного описания вакансии, посредством обращения к OpenAI API
    public void fetchGenerateDescriptionAndUpdateEntity(Vacancy vacancy) {
        //Получение сгенерированного краткого описания, на основе описания полного
        String generatedDescription = openAiApiFeignService
                .generateDescription(vacancy.getDescription());
        //Обновление и сохранение данных вакансии
        vacancyRepository.save(vacancy.setGeneratedDescription(generatedDescription));
    }

    public String fetchGenerateCoverLetter(Long vacancyHhId) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findByHhIdAndDescriptionNotNull(vacancyHhId);
        if (vacancyOptional.isPresent()) {
            Vacancy vacancy = vacancyOptional.get();
            //Получение сгенерированного сопроводительного письма, на основе полного описания
            String coverLetter = openAiApiFeignService
                    .generateCoverLetter(vacancy.getDescription());
            return coverLetter;
        }
        return null;

    }
}
