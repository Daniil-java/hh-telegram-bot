package com.education.hhtelegrambot.services;

import com.education.hhtelegrambot.entities.Vacancy;
import com.education.hhtelegrambot.entities.VacancyStatus;
import com.education.hhtelegrambot.entities.WorkFilter;
import com.education.hhtelegrambot.dtos.hh.HhEmployerDto;
import com.education.hhtelegrambot.dtos.hh.HhResponseDto;
import com.education.hhtelegrambot.dtos.hh.HhSimpleResponseDto;
import com.education.hhtelegrambot.repositories.VacancyRepository;
import com.education.hhtelegrambot.services.feign.OpenAiApiFeignService;
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

    public List<Vacancy> getAllByVacancyStatus(VacancyStatus vacancyStatus) {
        return vacancyRepository.findAllByStatus(vacancyStatus);
    }
    public List<Vacancy> getAllUnprocessedVacancies() {
        return vacancyRepository.findAllByNameIsNull();
    }

    public List<Vacancy> getAllUngeneratedVacancies() {
        return vacancyRepository.findAllByGeneratedDescriptionIsNullAndDescriptionIsNotNull();
    }

    public List<Vacancy> findByNotificationAttemptCountLessThan(int count) {
        return vacancyRepository.findProcessedVacanciesWithAttemptsLessThan(count);
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
                        .setWorkFilter(workFilter)
                        .setStatus(VacancyStatus.CREATED)
                );
            }
        }
    }


    //Обработка незаполненых вакансий, посредством обращения к api
    public void fetchAndSaveEntity(Vacancy vacancy) {
        //Получение ДТО-вакансии обращением к api
        HhResponseDto responseDto = hhApiService.getHhVacancyDtoByHhId(vacancy.getHhId());
        HhEmployerDto hhEmployerDto = hhApiService.getHhEmployerDtoByHhId(responseDto.getEmployer().getId());
        //Конвертация keySkills в String
        StringBuilder builder = new StringBuilder();
        if (responseDto.getKeySkills() != null) {
            for (String skill: responseDto.getKeySkills()) {
                builder.append(skill).append("|");
            }
        }

        //Конвертация ДТО в сущность вакансии и сохранение
        vacancyRepository.save(vacancy.setName(responseDto.getName())
                .setExperience(responseDto.getExperience().getName())
                .setKeySkills(builder.toString())
                .setEmployment(responseDto.getEmployment().getName())
                .setDescription(responseDto.getDescription())
                .setEmployerDescription(hhEmployerDto.getDescription())
                .setStatus(VacancyStatus.PARSED)
        );
    }

    //Сохранение сгенерированного описания вакансии, посредством обращения к OpenAI API
    public void fetchGenerateDescriptionAndUpdateEntity(Vacancy vacancy) {
        //Получение сгенерированного краткого описания, на основе описания полного
        String generatedDescription = openAiApiFeignService
                .generateDescription(vacancy.getDescription());
        //Обновление и сохранение данных вакансии
        vacancyRepository.save(vacancy
                .setGeneratedDescription(generatedDescription)
                .setStatus(VacancyStatus.PROCESSED)
        );
    }

    public String fetchGenerateCoverLetter(Long vacancyId, String userInfo) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findByIdAndDescriptionNotNull(vacancyId);
        if (vacancyOptional.isPresent()) {
            Vacancy vacancy = vacancyOptional.get();
            //Получение сгенерированного сопроводительного письма, на основе полного описания
            StringBuilder builder = new StringBuilder();
            builder.append("Ключевые навыки: ").append(vacancy.getKeySkills()).append("\n");
            builder.append(vacancy.getDescription());
            String coverLetter = openAiApiFeignService
                    .generateCoverLetter(builder.toString(),
                            userInfo, vacancy.getEmployerDescription());
            return coverLetter;
        }
        return null;
    }

    public void updateStatusById(Long vacancyId, VacancyStatus vacancyStatus) {
        vacancyRepository.updateStatusById(vacancyId, vacancyStatus);
    }

    public void vacancyRejectById(long vacancyId) {
        vacancyRepository.updateStatusById(vacancyId, VacancyStatus.REJECTED);
    }
}
