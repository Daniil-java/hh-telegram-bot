package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.repositories.VacancyRepository;
import com.education.hh_telegram_bot.services.feign.HhApiFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyService {
    private final HhApiFeignService hhApiFeignService;
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> saveAll(Long workFilterId, List<String> urls) {
        return vacancyRepository.saveAll(parseVacanciesFromUrls(workFilterId, urls));
    }

    public List<Vacancy> saveAll(List<Vacancy> vacancies) {
        return vacancyRepository.saveAll(vacancies);
    }

    private List<Vacancy> parseVacanciesFromUrls(Long workFilterId, List<String> urls) {
        List<Vacancy> vacancyList = new ArrayList<>();
        for (String url: urls) {
            Vacancy vacancy = parseVacancyInfo(url);
            if (vacancy != null) {
                vacancyList.add(vacancy.setWorkFilter(new WorkFilter().setId(workFilterId)));
            }
        }
        return vacancyList;
    }

    private Vacancy parseVacancyInfo(String url) {
        //Заглушка
        if (url.contains("click")) return null;
        String id = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
        return hhApiFeignService.getVacancyByHhId(id);
    }

    public List<Vacancy> getAllUnprocessedVacancies() {
        return vacancyRepository.findAllByNameNull();
    }

    public List<Vacancy> getAllUngeneratedVacancies() {
        return vacancyRepository.findAllByGeneratedDescriptionNull();
    }

    public List<Vacancy> getAllUnsentVacancies(Long workFilterId) {
        List<Vacancy> list = vacancyRepository.findAllByIsSentFalseAndWorkFilterId(workFilterId);
        for (Vacancy vacancy: list) {
            vacancy.setSent(true);
        }
        return vacancyRepository.saveAll(list);
    }
}
