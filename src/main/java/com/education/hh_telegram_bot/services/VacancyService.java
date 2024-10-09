package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.repositories.VacancyRepository;
import com.education.hh_telegram_bot.services.feign.HhApiFeignService;
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

    public List<Vacancy> getAllUnprocessedVacancies() {
        return vacancyRepository.findAllByNameIsNull();
    }

    public List<Vacancy> getAllUngeneratedVacancies() {
        return vacancyRepository.findAllByGeneratedDescriptionIsNull();
    }

    public List<Vacancy> getAllUnsentVacancies(Long workFilterId) {
        return vacancyRepository.findAllByIsSentFalseAndWorkFilterId(workFilterId);
    }

    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }
}
